package com.example.langualink.ui.learn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.data.repository.LessonRepository
import com.example.langualink.model.Language // Assuming Language model exists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.langualink.data.local.dao.LanguageDao
import com.example.langualink.data.local.dao.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// State for the LearnScreen Top Bar
data class LearnTopBarState(
    val currentLanguageName: String? = null,
    val currentLanguageId: Int? = null,
    val currentLevel: String? = null,
    val points: Int = 0,
    val availableLanguages: List<Language> = emptyList(),
    val availableLevels: List<String> = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
    val isLoading: Boolean = true
)

data class LearnScreenState(
    val currentChapter: com.example.langualink.model.Chapter? = null,
    val currentLesson: com.example.langualink.model.Lesson? = null,
    val chapterProgress: Int = 0,
    val totalExercisesInChapter: Int = 0,
    val exercises: List<com.example.langualink.model.Exercise> = emptyList(),
    val completedExerciseIds: List<Int> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val userDao: UserDao,
    private val languageDao: LanguageDao,
    private val exerciseDao: ExerciseDao,
    private val lessonRepository: LessonRepository
) : ViewModel() {

    // Internal mutable state flow
    private val _topBarState = MutableStateFlow(LearnTopBarState())
    // Public read-only state flow for the UI
    val topBarState: StateFlow<LearnTopBarState> = _topBarState.asStateFlow()

    private val _screenState = MutableStateFlow(LearnScreenState())
    val screenState: StateFlow<LearnScreenState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                Log.d("LearnViewModel", "User: $user")
                if (user != null) {
                    val languages = languageDao.getAllLanguages().first()

                    _topBarState.update {
                        it.copy(
                            currentLanguageName = languages.find { it.id == user.currentLanguageId }?.name,
                            currentLanguageId = user.currentLanguageId,
                            currentLevel = user.currentLevel.name,
                            points = user.points,
                            availableLanguages = languages,
                            isLoading = false
                        )
                    }
                    loadChaptersAndExercises(user.currentLanguageId, user.currentLevel, user.completedExerciseIds)
                }
            }
        }
    }

    private fun loadChaptersAndExercises(languageId: Int, level: com.example.langualink.model.Level, completedExerciseIds: List<Int>) {
        viewModelScope.launch {
            val lesson = lessonRepository.getLessonByChapterId(level.ordinal).first()
            exerciseDao.getExercisesByLanguageAndLevel(languageId, level).collect { exercises ->
                Log.d("LearnViewModel", "Exercises: $exercises")
                val chapters = exercises.groupBy { it.id / 10 }.map { (chapterId, chapterExercises) ->
                    com.example.langualink.model.Chapter(
                        id = chapterId,
                        title = "Chapter ${chapterId + 1}",
                        exercises = chapterExercises
                    )
                }
                Log.d("LearnViewModel", "Chapters: $chapters")

                val currentChapter = chapters.firstOrNull { chapter ->
                    chapter.exercises.any { exercise -> exercise.id !in completedExerciseIds }
                } ?: chapters.lastOrNull()

                Log.d("LearnViewModel", "Current Chapter: $currentChapter")

                if (currentChapter != null) {
                    val completedInChapter = currentChapter.exercises.count { it.id in completedExerciseIds }
                    _screenState.update {
                        it.copy(
                            currentChapter = currentChapter,
                            currentLesson = lesson,
                            chapterProgress = completedInChapter,
                            totalExercisesInChapter = currentChapter.exercises.size,
                            exercises = currentChapter.exercises,
                            completedExerciseIds = completedExerciseIds,
                            isLoading = false
                        )
                    }
                } else {
                    _screenState.update { it.copy(isLoading = false, exercises = emptyList(), completedExerciseIds = completedExerciseIds, currentLesson = lesson) }
                }
                Log.d("LearnViewModel", "Screen State: ${_screenState.value}")
            }
        }
    }

    // Function to update the selected language (UI state only for now)
    fun changeLanguage(newLanguageId: Int) {
        viewModelScope.launch {
            val user = userDao.getUser().first()
            if (user != null) {
                userDao.insertOrUpdateUser(user.copy(currentLanguageId = newLanguageId))
            }
        }
    }

    // Function to update the selected level (UI state only for now)
    fun changeLevel(newLevelString: String) {
        viewModelScope.launch {
            val user = userDao.getUser().first()
            if (user != null) {
                userDao.insertOrUpdateUser(user.copy(currentLevel = com.example.langualink.model.Level.valueOf(newLevelString)))
            }
        }
    }
}