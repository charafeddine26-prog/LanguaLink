package com.example.langualink.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.langualink.data.local.Converters
import com.example.langualink.data.local.dao.BadgeDao
import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.data.local.dao.LanguageDao
import com.example.langualink.data.local.dao.UserDao
import com.example.langualink.model.Badge
import com.example.langualink.model.Exercise
import com.example.langualink.model.Language
import com.example.langualink.model.User

@Database(
    entities = [User::class, Language::class, Badge::class, Exercise::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun languageDao(): LanguageDao
    abstract fun badgeDao(): BadgeDao
    abstract fun exerciseDao(): ExerciseDao
}
