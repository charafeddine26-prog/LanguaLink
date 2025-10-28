package com.example.langualink.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.local.dao.ExerciseDao
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



@HiltViewModel
class LearnViewModel @Inject constructor(
    private val userDao: UserDao,
    private val languageDao: LanguageDao,
    private val exerciseDao: ExerciseDao
) : ViewModel() {

    // Internal mutable state flow
    private val _topBarState = MutableStateFlow(LearnTopBarState())
    // Public read-only state flow for the UI
    val topBarState: StateFlow<LearnTopBarState> = _topBarState.asStateFlow()

    val chapters = MutableStateFlow<List<com.example.langualink.model.Chapter>>(emptyList())

    init {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                if (user != null) {
                    val languages = languageDao.getAllLanguages().first()
                    _topBarState.update {
                        it.copy(
                            currentLanguageName = languages.find { it.id == user.currentLanguageId }?.name,
                            currentLanguageId = user.currentLanguageId,
                            currentLevel = user.currentLevel.name,
                            points = 500, // TODO: Get points from user
                            availableLanguages = languages,
                            isLoading = false
                        )
                    }
                    loadChapters(user.currentLanguageId, user.currentLevel)
                }
            }
        }
    }

    private fun loadChapters(languageId: Int, level: com.example.langualink.model.Level) {
        viewModelScope.launch {
            exerciseDao.getExercisesByLanguageAndLevel(languageId, level).collect { exercises ->
                val chapters = exercises.groupBy { it.id / 10 }.map { (chapterId, exercises) ->
                    com.example.langualink.model.Chapter(
                        id = chapterId,
                        title = "Chapter $chapterId",
                        exercises = exercises
                    )
                }
                this@LearnViewModel.chapters.value = chapters
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