package com.example.langualink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: Int,
    val languageId: Int,
    val level: Level,
    val type: ExerciseType,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

enum class ExerciseType {
    MULTIPLE_CHOICE,
    TRANSLATION,
    AUDIO,
    VIDEO
}
