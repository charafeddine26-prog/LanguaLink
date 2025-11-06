package com.example.langualink.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.repository.BadgeRepository
import com.example.langualink.data.repository.ExerciseRepository
import com.example.langualink.data.repository.UserRepository
import com.example.langualink.model.Badge
import com.example.langualink.model.Level
import com.example.langualink.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Define a UI state class to encapsulate all required data
data class ProfileUiState(
    val user: User? = null,
    val levelProgressList: List<LevelProgress> = emptyList(),
    val earnedBadges: List<Badge> = emptyList(),
    val totalPoints: Int = 0,
    val isLoading: Boolean = true
)

// 2. Define a class to store the progress for each level
data class LevelProgress(
    val level: Level,
    val completedCount: Int,
    val totalCount: Int
)

@HiltViewModel
class ProgressionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    // 4. Change the StateFlow type to our new UiState
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            badgeRepository.getUserWithBadges(1).collect { userWithBadges ->
                if (userWithBadges != null) {
                    val user = userWithBadges.user
                    val badges = userWithBadges.badges
                    val levelProgress = loadLevelProgress(user)

                    _uiState.update {
                        it.copy(
                            user = user,
                            levelProgressList = levelProgress,
                            earnedBadges = badges,
                            totalPoints = user.points,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     * Load progress for all levels
     */
    private suspend fun loadLevelProgress(user: User): List<LevelProgress> {
        val progressList = mutableListOf<LevelProgress>()
        val allLevels = Level.values() // Get A1, A2, B1...

        for (level in allLevels) {
            // Get all exercises for this language and level from the database
            val exercisesForLevel = exerciseRepository
                .getExercisesByLanguageAndLevel(user.currentLanguageId, level)
                .first() // .first() to get the current value from the Flow

            // Calculate the number of completed exercises
            val completedCount = exercisesForLevel.count { exercise ->
                exercise.id in user.completedExerciseIds
            }

            progressList.add(
                LevelProgress(
                    level = level,
                    completedCount = completedCount,
                    totalCount = exercisesForLevel.size
                )
            )
        }
        return progressList
    }
}