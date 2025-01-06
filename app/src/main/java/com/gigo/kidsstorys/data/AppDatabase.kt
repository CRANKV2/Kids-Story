package com.gigo.kidsstorys.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gigo.kidsstorys.data.dao.StoryDao
import com.gigo.kidsstorys.data.dao.ChatMessageDao
import com.gigo.kidsstorys.data.models.Story
import com.gigo.kidsstorys.data.models.ChatMessageEntity
import com.gigo.kidsstorys.data.migrations.MIGRATION_1_2

@Database(
    entities = [
        Story::class,
        ChatMessageEntity::class
    ],
    version = 2,
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
                    "kidsstorys_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 