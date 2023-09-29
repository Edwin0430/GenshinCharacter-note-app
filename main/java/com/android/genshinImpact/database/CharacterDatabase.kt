package com.android.genshinImpact.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.genshinImpact.Character

@Database(entities = [Character::class], version = 3)
@TypeConverters(CharacterTypeConverters::class)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        // Define your migration from version 1 to version 2 here
        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE `com.android.genshinImpact.Character` ADD COLUMN `owner` TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        private val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform the necessary database schema migration from version 2 to version 3
                // For example, you can add a new column to the existing table
                database.execSQL(
                    "ALTER TABLE `com.android.genshinImpact.Character` ADD COLUMN `time` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        // Create a singleton of the CharacterDatabase
        @Volatile
        private var instance: CharacterDatabase? = null

        fun getInstance(context: Context): CharacterDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CharacterDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CharacterDatabase::class.java,
                "character_database"
            )
                // Add both migrations to the database builder
                .addMigrations(migration_1_2, migration_2_3)
                .build()
        }
    }
}

