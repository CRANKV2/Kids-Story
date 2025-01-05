package com.gigo.testapp

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.gigo.testapp.data.StoryDatabase
import com.gigo.testapp.data.UserPreferencesRepository
import androidx.room.Room
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.PreferencesSerializer
import java.io.File
import java.util.prefs.PreferencesFactory

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