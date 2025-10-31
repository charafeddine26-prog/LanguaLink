package com.example.langualink.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.langualink.model.Language
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguages(languages: List<Language>)

    @Query("SELECT * FROM languages")
    fun getAllLanguages(): Flow<List<Language>>

    @Query("SELECT * FROM languages WHERE name = :name")
    fun getLanguageByName(name: String): Flow<List<Language>>
}
