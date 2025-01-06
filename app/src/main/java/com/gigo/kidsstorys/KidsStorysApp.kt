package com.gigo.kidsstorys

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.gigo.kidsstorys.data.AppDatabase
import com.gigo.kidsstorys.data.UserPreferencesRepository

class KidsStorysApp : Application() {
    // DataStore für User Preferences
    private val dataStore by preferencesDataStore(name = "user_preferences")
    
    // Properties für Database und UserPreferences
    lateinit var database: AppDatabase
        private set
        
    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    companion object {
        private lateinit var instance: KidsStorysApp
        
        fun getInstance(): KidsStorysApp {
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