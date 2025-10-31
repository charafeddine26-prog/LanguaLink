package com.example.langualink.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithBadges(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = UserBadgeCrossRef::class,
            parentColumn = "userId",
            entityColumn = "badgeId"
        )
    )
    val badges: List<Badge>
)