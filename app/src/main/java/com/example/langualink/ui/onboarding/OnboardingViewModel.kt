package com.example.langualink.ui.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

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
}