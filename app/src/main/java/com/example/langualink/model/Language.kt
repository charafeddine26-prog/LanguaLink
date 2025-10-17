package com.example.langualink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class Language(
    @PrimaryKey val id: Int,
    val name: String
)
