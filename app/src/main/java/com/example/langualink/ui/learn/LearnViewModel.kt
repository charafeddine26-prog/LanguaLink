package com.example.langualink.ui.learn

import androidx.lifecycle.ViewModel
import com.example.langualink.model.Language // Assuming Language model exists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// State for the LearnScreen Top Bar
data class LearnTopBarState(
    val currentLanguageName: String = "Français", // Default language
    val currentLanguageId: Int = 1, // Default language ID (mock)
    val currentLevel: String = "B2", // Default level
    val points: Int = 500, // Default points
    val availableLanguages: List<Language> = listOf( // Mock language list
        Language(1, "Français"),
        Language(2, "Español"),
        Language(3, "Deutsch")
        // Add more mock languages if needed
    ),
    val availableLevels: List<String> = listOf("A1", "A2", "B1", "B2", "C1", "C2"), // Level options
    val isLoading: Boolean = false
)

@HiltViewModel
class LearnViewModel @Inject constructor() : ViewModel() {

    // Internal mutable state flow
    private val _topBarState = MutableStateFlow(LearnTopBarState())
    // Public read-only state flow for the UI
    val topBarState: StateFlow<LearnTopBarState> = _topBarState.asStateFlow()

    // Function to update the selected language (UI state only for now)
    fun changeLanguage(newLanguageId: Int) {
        val newLanguage = _topBarState.value.availableLanguages.find { it.id == newLanguageId }
        newLanguage?.let {
            _topBarState.update { currentState ->
                currentState.copy(
                    currentLanguageId = it.id,
                    currentLanguageName = it.name
                )
            }
            // In the future, this would also trigger reloading lessons
        }
    }

    // Function to update the selected level (UI state only for now)
    fun changeLevel(newLevelString: String) {
        if (_topBarState.value.availableLevels.contains(newLevelString)) {
            _topBarState.update { currentState ->
                currentState.copy(currentLevel = newLevelString)
            }
            // In the future, this would also trigger reloading lessons
        }
    }
}