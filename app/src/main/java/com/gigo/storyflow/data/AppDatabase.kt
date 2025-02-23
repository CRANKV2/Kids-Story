package com.gigo.storyflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gigo.storyflow.data.dao.ChatMessageDao
import com.gigo.storyflow.data.dao.StoryDao
import com.gigo.storyflow.data.migrations.MIGRATION_1_2
import com.gigo.storyflow.data.migrations.MIGRATION_2_3
import com.gigo.storyflow.data.models.ChatMessageEntity
import com.gigo.storyflow.data.models.Story

@Database(
    entities = [
        Story::class,
        ChatMessageEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "storyflow_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 