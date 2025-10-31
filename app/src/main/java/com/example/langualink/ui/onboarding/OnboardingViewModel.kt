package com.example.langualink.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.local.SettingsDataStore
import com.example.langualink.data.local.dao.LanguageDao
import com.example.langualink.data.local.dao.UserDao
import com.example.langualink.model.Level
import com.example.langualink.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDao: UserDao,
    private val languageDao: LanguageDao,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    // Internal state for the selected language
    private val _selectedLanguage = MutableStateFlow<String?>(null)

    // Public state flow for the UI to observe
    val selectedLanguage = _selectedLanguage.asStateFlow()

    val languages = languageDao.getAllLanguages()

    /**
     * Called when the user selects a language from the list.
     * @param language The name of the selected language.
     */
    fun selectLanguage(language: String) {
        _selectedLanguage.value = language
    }

    // Internal state for the user's name
    private val _username = MutableStateFlow("")
    // Public state flow for the UI to observe
    val username = _username.asStateFlow()

    fun setUsername(name: String) {
        _username.value = name
    }

    // Internal state for the selected level
    private val _selectedLevel = MutableStateFlow<String?>(null)

    // Public state flow for the UI to observe
    val selectedLevel = _selectedLevel.asStateFlow()

    /**
     * Called when the user selects a level.
     * @param level The selected proficiency level (e.g., "A1", "B2").
     */
    fun selectLevel(level: String) {
        _selectedLevel.value = level
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted = _onboardingCompleted.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.onboardingCompleted.first().let { completed ->
                _onboardingCompleted.value = completed
                _isLoading.value = false
            }
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setOnboardingCompleted(completed)
        }
    }

    fun saveUser() {
        viewModelScope.launch {
            val languageId = withContext(Dispatchers.IO) {
                languageDao.getLanguageByName(_selectedLanguage.value!!).first().first().id
            }
            val user = User(
                name = _username.value,
                currentLanguageId = languageId,
                currentLevel = Level.valueOf(_selectedLevel.value!!),
                completedExerciseIds = emptyList(),
                earnedBadgeIds = emptyList()
            )
            userDao.insertOrUpdateUser(user)
        }
    }
}