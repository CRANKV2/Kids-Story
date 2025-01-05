package com.gigo.kidsstorys

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.gigo.kidsstorys.data.StoryDatabase
import com.gigo.kidsstorys.data.UserPreferencesRepository
import androidx.room.Room

class MyApplication : Application() {
    lateinit var database: StoryDatabase
    lateinit var userPreferencesRepository: UserPreferencesRepository

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