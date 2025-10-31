package com.example.langualink.ui.learn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.data.local.dao.UserDao
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
    private val exerciseDao: ExerciseDao,
    private val userDao: UserDao,
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
            exercises = exerciseDao.getExercisesByChapterIdAndLevel(chapterId, level).first()
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
                    val user = userDao.getUser().first()
                    if (user != null) {
                        val completedExerciseIds = user.completedExerciseIds.toMutableList()
                        if (exercise.id !in completedExerciseIds) {
                            completedExerciseIds.add(exercise.id)
                            userDao.insertOrUpdateUser(user.copy(completedExerciseIds = completedExerciseIds))
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