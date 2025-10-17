package com.example.langualink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class Badge(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val iconResId: Int
)
