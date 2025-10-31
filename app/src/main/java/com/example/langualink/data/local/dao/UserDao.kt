package com.example.langualink.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.langualink.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: User)

    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): Flow<User?>

    @Query("UPDATE users SET points = points + :points WHERE id = 1")
    suspend fun addPoints(points: Int)
}
