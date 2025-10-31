package com.example.langualink.ui.learn

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.example.langualink.data.repository.ExerciseRepository
import com.example.langualink.model.Chapter
import com.example.langualink.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

import com.example.langualink.data.repository.UserRepository
import com.example.langualink.data.repository.BadgeRepository

@HiltViewModel
class ChapterDetailsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _chapter = MutableStateFlow<Chapter?>(null)
    val chapter: StateFlow<Chapter?> = _chapter

    fun loadChapter(chapterId: Int, languageId: Int, level: com.example.langualink.model.Level) {
        viewModelScope.launch {
            val exercises = exerciseRepository.getExercisesByLanguageAndLevel(languageId, level).first()
            _chapter.value = Chapter(chapterId, "Chapter $chapterId", exercises)
        }
    }

    private val _bravoState = MutableStateFlow<BravoState?>(null)
    val bravoState: StateFlow<BravoState?> = _bravoState

    fun showBravoModal(points: Int, badgeName: String?) {
        _bravoState.value = BravoState(points, badgeName)
    }

    fun hideBravoModal() {
        _bravoState.value = null
    }

    data class BravoState(val points: Int, val badgeName: String?)

    fun markExerciseAsComplete(exerciseId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUser().first()
            if (user != null) {
                val completedExerciseIds = user.completedExerciseIds.toMutableList()
                if (!completedExerciseIds.contains(exerciseId)) {
                    completedExerciseIds.add(exerciseId)
                    userRepository.insertOrUpdateUser(user.copy(completedExerciseIds = completedExerciseIds))
                    showBravoModal(10, null) // Show bravo modal with 10 points
                    onChapterCompleted()
                }
            }
        }
    }

    private fun onChapterCompleted() {
        viewModelScope.launch {
            val user = userRepository.getUser().first()
            val chapter = _chapter.value
            if (user != null && chapter != null) {
                val completedInThisChapter = user.completedExerciseIds.intersect(chapter.exercises.map { it.id }.toSet())
                if (completedInThisChapter.size == chapter.exercises.size) {
                    // Chapter completed
                    val completedChapters = user.completedChapterIds.toMutableList()
                    if (!completedChapters.contains(chapter.id)) {
                        completedChapters.add(chapter.id)
                        userRepository.insertOrUpdateUser(user.copy(completedChapterIds = completedChapters))
                        userRepository.addPoints(50) // Add 50 points for completing a chapter

                        if (user.completedChapterIds.isEmpty()) {
                            // Award "First Chapter Completed" badge
                            badgeRepository.awardBadge(user.id, 3)
                            userRepository.addPoints(100) // Add 100 bonus points for the first chapter
                        }
                    }
                }
            }
        }
    }
}
