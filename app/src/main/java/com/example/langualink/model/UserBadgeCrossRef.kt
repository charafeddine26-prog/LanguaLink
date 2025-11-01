package com.example.langualink.model

import androidx.room.Entity

@Entity(tableName = "user_badge_cross_ref", primaryKeys = ["userId", "badgeId"])
data class UserBadgeCrossRef(
    val userId: Int,
    val badgeId: Int
)