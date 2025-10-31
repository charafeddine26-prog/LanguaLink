package com.example.langualink.data.repository

import com.example.langualink.data.local.dao.BadgeDao
import com.example.langualink.model.Badge
import com.example.langualink.model.UserBadgeCrossRef
import com.example.langualink.model.UserWithBadges
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BadgeRepository @Inject constructor(private val badgeDao: BadgeDao) {

    fun getAllBadges(): Flow<List<Badge>> = badgeDao.getAllBadges()

    suspend fun insertBadges(badges: List<Badge>) {
        badgeDao.insertBadges(badges)
    }

    suspend fun awardBadge(userId: Int, badgeId: Int) {
        badgeDao.insertUserBadge(UserBadgeCrossRef(userId, badgeId))
    }

    fun getUserWithBadges(userId: Int): Flow<UserWithBadges?> {
        return badgeDao.getUserWithBadges(userId)
    }
}