package com.gigo.storyflow

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.gigo.storyflow.data.AppDatabase
import com.gigo.storyflow.data.UserPreferencesRepository

class StoryFlowApp : Application() {
    // DataStore für User Preferences
    private val dataStore by preferencesDataStore(name = "user_preferences")
    
    // Properties für Database und UserPreferences
    lateinit var database: AppDatabase
        private set
        
    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    companion object {
        private lateinit var instance: StoryFlowApp
        
        fun getInstance(): StoryFlowApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialisierung der Datenbank
        database = AppDatabase.getDatabase(this)
        
        // Initialisierung des UserPreferencesRepository
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
} 