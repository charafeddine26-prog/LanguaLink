package com.example.langualink.ui.learn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.repository.BadgeRepository
import com.example.langualink.data.repository.ExerciseRepository
import com.example.langualink.data.repository.UserRepository
import com.example.langualink.model.Exercise
import com.example.langualink.model.Level
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseScreenState(
    val exercise: Exercise? = null,
    val selectedOption: String? = null,
    val isAnswerCorrect: Boolean? = null,
    val isLoading: Boolean = true,
    val showCompletionModal: Boolean = false
)

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository,
    private val badgeRepository: BadgeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow(ExerciseScreenState())
    val screenState = _screenState.asStateFlow()

    val chapterId: Int = (savedStateHandle.get<String>("chapterId")!!).toInt()
    val exerciseId: Int = (savedStateHandle.get<String>("exerciseId")!!).toInt()

    val level: Level = Level.valueOf(savedStateHandle.get<String>("level")!!)

    private var exercises: List<Exercise> = emptyList()

    val timer = flow {
        for (i in 30 downTo 0) {
            emit(i)
            delay(1000)
        }
    }

    init {
        viewModelScope.launch {
            exercises = exerciseRepository.getExercisesByChapterId(chapterId).first()
            val exercise = exercises.find { it.id == exerciseId }
            _screenState.value = ExerciseScreenState(exercise = exercise, isLoading = false)
        }
    }

    fun selectOption(option: String) {
        val exercise = _screenState.value.exercise
        if (exercise != null) {
            val isCorrect = exercise.correctAnswer == option
            _screenState.value = _screenState.value.copy(selectedOption = option, isAnswerCorrect = isCorrect)
            if (isCorrect) {
                viewModelScope.launch {
                    val user = userRepository.getUser().first()
                    if (user != null) {
                        val completedExerciseIds = user.completedExerciseIds.toMutableList()
                        if (!completedExerciseIds.contains(exercise.id)) {
                            completedExerciseIds.add(exercise.id)
                            userRepository.insertOrUpdateUser(user.copy(completedExerciseIds = completedExerciseIds))
                            userRepository.addPoints(10) // Add 10 points for completing an exercise

                            if (user.completedExerciseIds.isEmpty()) {
                                // Award "First Lesson Completed" badge
                                badgeRepository.awardBadge(user.id, 2)
                                userRepository.addPoints(20) // Add 20 bonus points for the first lesson
                            }
                        }
                    }
                }
            }
        }
    }

    fun getNextExercise(): Exercise? {
        val currentExerciseIndex = exercises.indexOfFirst { it.id == exerciseId }
        return if (currentExerciseIndex != -1 && currentExerciseIndex < exercises.size - 1) {
            exercises[currentExerciseIndex + 1]
        } else {
            null
        }
    }

    fun showCompletionModal() {
        _screenState.update { it.copy(showCompletionModal = true) }
    }

    fun dismissCompletionModal() {
        _screenState.update { it.copy(showCompletionModal = false) }
    }
}