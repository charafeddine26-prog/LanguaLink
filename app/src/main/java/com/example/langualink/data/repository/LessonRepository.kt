package com.example.langualink.data.repository

import com.example.langualink.data.local.dao.LessonDao
import com.example.langualink.model.Lesson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepository @Inject constructor(private val lessonDao: LessonDao) {

    fun getLessonByChapterId(chapterId: Int): Flow<Lesson?> =
        lessonDao.getLessonByChapterId(chapterId)
}
