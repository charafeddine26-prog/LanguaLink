package com.example.langualink.data.repository

import com.example.langualink.data.local.dao.UserDao
import com.example.langualink.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    fun getUser(): Flow<User?> = userDao.getUser()

    suspend fun insertOrUpdateUser(user: User) {
        userDao.insertOrUpdateUser(user)
    }

    suspend fun addPoints(points: Int) {
        userDao.addPoints(points)
    }
}