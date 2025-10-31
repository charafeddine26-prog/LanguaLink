package com.example.langualink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int = 1, // We'll only have one user
    val name: String,
    val currentLanguageId: Int,
    val currentLevel: Level,
    val completedExerciseIds: List<Int>,
    val points: Int = 0,
    val completedChapterIds: List<Int> = emptyList()
)
