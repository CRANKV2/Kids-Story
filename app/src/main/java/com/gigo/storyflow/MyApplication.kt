package com.gigo.storyflow

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.gigo.storyflow.data.StoryDatabase
import com.gigo.storyflow.data.UserPreferencesRepository

class MyApplication : Application() {
    lateinit var database: StoryDatabase
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private val dataStore by preferencesDataStore(name = "user_preferences")

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        database = Room.databaseBuilder(
            applicationContext,
            StoryDatabase::class.java,
            "story_database"
        ).build()
        
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
} 