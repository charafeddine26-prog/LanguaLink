package com.example.langualink.ui.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.langualink.model.Level

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    // Internal state for the selected language
    private val _selectedLanguage = MutableStateFlow<String?>(null)

    // Public state flow for the UI to observe
    val selectedLanguage = _selectedLanguage.asStateFlow()

    /**
     * Called when the user selects a language from the list.
     * @param language The name of the selected language.
     */
    fun selectLanguage(language: String) {
        _selectedLanguage.value = language
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
}