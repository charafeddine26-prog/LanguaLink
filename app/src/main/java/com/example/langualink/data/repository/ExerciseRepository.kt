package com.example.langualink.data.repository

import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.model.Exercise
import com.example.langualink.model.Level
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(private val exerciseDao: ExerciseDao) {

    fun getExercisesByChapterId(chapterId: Int): Flow<List<Exercise>> =
        exerciseDao.getExercisesByChapterId(chapterId)

    fun getExercisesByLanguageAndLevel(languageId: Int, level: Level): Flow<List<Exercise>> =
        exerciseDao.getExercisesByLanguageAndLevel(languageId, level)
}