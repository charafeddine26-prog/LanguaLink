package com.example.langualink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey val id: Int,
    val chapterId: Int,
    val title: String,
    val content: String
)
