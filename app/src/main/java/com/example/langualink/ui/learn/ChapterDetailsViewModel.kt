package com.example.langualink.ui.learn

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.model.Chapter
import com.example.langualink.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

import com.example.langualink.data.local.dao.UserDao

@HiltViewModel
class ChapterDetailsViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val userDao: UserDao
) : ViewModel() {

    private val _chapter = MutableStateFlow<Chapter?>(null)
    val chapter: StateFlow<Chapter?> = _chapter

    fun loadChapter(chapterId: Int, languageId: Int, level: com.example.langualink.model.Level) {
        viewModelScope.launch {
            val exercises = exerciseDao.getExercisesByLanguageAndLevel(languageId, level).first()
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
            val user = userDao.getUser().first()
            if (user != null) {
                val completedExerciseIds = user.completedExerciseIds.toMutableList()
                if (!completedExerciseIds.contains(exerciseId)) {
                    completedExerciseIds.add(exerciseId)
                    userDao.insertOrUpdateUser(user.copy(completedExerciseIds = completedExerciseIds))
                    showBravoModal(10, null) // Show bravo modal with 10 points
                }
            }
        }
    }
}
