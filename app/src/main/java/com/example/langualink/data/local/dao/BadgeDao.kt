package com.example.langualink.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.langualink.model.Badge
import com.example.langualink.model.UserBadgeCrossRef
import com.example.langualink.model.UserWithBadges
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<Badge>)

    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<Badge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBadge(userBadge: UserBadgeCrossRef)

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithBadges(userId: Int): Flow<UserWithBadges?>
}
