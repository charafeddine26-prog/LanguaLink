package com.example.langualink.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.langualink.data.local.Converters
import com.example.langualink.data.local.dao.BadgeDao
import com.example.langualink.data.local.dao.ExerciseDao
import com.example.langualink.data.local.dao.LanguageDao
import com.example.langualink.data.local.dao.UserDao
import com.example.langualink.model.Badge
import com.example.langualink.model.Exercise
import com.example.langualink.model.Language
import com.example.langualink.model.User
import com.example.langualink.model.UserBadgeCrossRef

@Database(
    entities = [User::class, Language::class, Badge::class, Exercise::class, UserBadgeCrossRef::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun languageDao(): LanguageDao
    abstract fun badgeDao(): BadgeDao
    abstract fun exerciseDao(): ExerciseDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN points INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `user_badge_cross_ref` (`userId` INTEGER NOT NULL, `badgeId` INTEGER NOT NULL, PRIMARY KEY(`userId`, `badgeId`))"
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE users ADD COLUMN completedChapterIds TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create a new table with the desired schema
        database.execSQL("CREATE TABLE `users_new` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `currentLanguageId` INTEGER NOT NULL, `currentLevel` TEXT NOT NULL, `completedExerciseIds` TEXT NOT NULL, `points` INTEGER NOT NULL, `completedChapterIds` TEXT NOT NULL, PRIMARY KEY(`id`))")
        // Copy the data from the old table to the new table
        database.execSQL("INSERT INTO `users_new` (`id`, `name`, `currentLanguageId`, `currentLevel`, `completedExerciseIds`, `points`, `completedChapterIds`) SELECT `id`, `name`, `currentLanguageId`, `currentLevel`, `completedExerciseIds`, `points`, `completedChapterIds` FROM `users`")
        // Remove the old table
        database.execSQL("DROP TABLE `users`")
        // Rename the new table to the original table name
        database.execSQL("ALTER TABLE `users_new` RENAME TO `users`")
    }
}
