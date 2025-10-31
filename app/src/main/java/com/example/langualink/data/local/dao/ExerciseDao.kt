package com.example.langualink.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.langualink.model.Exercise
import com.example.langualink.model.Level
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Query("SELECT * FROM exercises WHERE languageId = :languageId AND level = :level")
    fun getExercisesByLanguageAndLevel(languageId: Int, level: Level): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id / 10 = :chapterId")
    fun getExercisesByChapterId(chapterId: Int): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id / 10 = :chapterId AND level = :level")
    fun getExercisesByChapterIdAndLevel(chapterId: Int, level: Level): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseById(exerciseId: Int): Flow<Exercise>
}
