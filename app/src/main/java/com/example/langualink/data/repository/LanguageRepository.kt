package com.example.langualink.data.repository

import com.example.langualink.data.local.dao.LanguageDao
import com.example.langualink.model.Language
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepository @Inject constructor(private val languageDao: LanguageDao) {

    fun getAllLanguages(): Flow<List<Language>> = languageDao.getAllLanguages()

    fun getLanguageByName(name: String): Flow<List<Language>> = languageDao.getLanguageByName(name)
}