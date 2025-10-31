package com.example.langualink.di

import com.example.langualink.data.local.dao.BadgeDao
import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.data.local.dao.LanguageDao
import com.example.langualink.data.local.dao.UserDao
import com.example.langualink.data.repository.BadgeRepository
import com.example.langualink.data.repository.ExerciseRepository
import com.example.langualink.data.repository.LanguageRepository
import com.example.langualink.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideBadgeRepository(badgeDao: BadgeDao): BadgeRepository {
        return BadgeRepository(badgeDao)
    }

    @Provides
    @Singleton
    fun provideLanguageRepository(languageDao: LanguageDao): LanguageRepository {
        return LanguageRepository(languageDao)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDao: ExerciseDao): ExerciseRepository {
        return ExerciseRepository(exerciseDao)
    }
}