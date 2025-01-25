package com.gigo.storyflow.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gigo.storyflow.data.models.Story

@Database(
    entities = [Story::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()