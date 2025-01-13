package com.gigo.kidsstorys.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val dataStore = context.dataStore
    private val CARD_ALPHA = floatPreferencesKey("card_alpha")
    private val DEFAULT_CARD_ALPHA = 0.75f

    var fontSize: Int
        get() = sharedPreferences.getInt(KEY_FONT_SIZE, DEFAULT_FONT_SIZE)
        set(value) = sharedPreferences.edit().putInt(KEY_FONT_SIZE, value).apply()

    var wrapText: Boolean
        get() = sharedPreferences.getBoolean(KEY_WRAP_TEXT, DEFAULT_WRAP_TEXT)
        set(value) = sharedPreferences.edit().putBoolean(KEY_WRAP_TEXT, value).apply()

    var titleSize: Int
        get() = sharedPreferences.getInt(KEY_TITLE_SIZE, DEFAULT_TITLE_SIZE)
        set(value) = sharedPreferences.edit().putInt(KEY_TITLE_SIZE, value).apply()

    var previewSize: Int
        get() = sharedPreferences.getInt(KEY_PREVIEW_SIZE, DEFAULT_PREVIEW_SIZE)
        set(value) = sharedPreferences.edit().putInt(KEY_PREVIEW_SIZE, value).apply()

    var isCompactView: Boolean
        get() = sharedPreferences.getBoolean(KEY_COMPACT_VIEW, true)
        set(value) = sharedPreferences.edit().putBoolean(KEY_COMPACT_VIEW, value).apply()

    val cardAlpha: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[CARD_ALPHA] ?: DEFAULT_CARD_ALPHA
        }

    companion object {
        private const val PREFS_NAME = "story_settings"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_WRAP_TEXT = "wrap_text"
        private const val KEY_TITLE_SIZE = "title_size"
        private const val KEY_PREVIEW_SIZE = "preview_size"
        private const val KEY_COMPACT_VIEW = "compact_view"
        
        private const val DEFAULT_FONT_SIZE = 16
        private const val DEFAULT_WRAP_TEXT = true
        private const val DEFAULT_TITLE_SIZE = 18
        private const val DEFAULT_PREVIEW_SIZE = 14

        @Volatile
        private var instance: SettingsManager? = null

        fun getInstance(context: Context): SettingsManager {
            return instance ?: synchronized(this) {
                instance ?: SettingsManager(context).also { instance = it }
            }
        }
    }

    fun updateFontSize(size: Int) {
        fontSize = size
    }

    fun updateWrapText(wrap: Boolean) {
        wrapText = wrap
    }

    fun updateTitleSize(size: Int) {
        titleSize = size
    }

    fun updatePreviewSize(size: Int) {
        previewSize = size
    }

    suspend fun updateCardAlpha(alpha: Float) {
        dataStore.edit { preferences ->
            preferences[CARD_ALPHA] = alpha
        }
    }
}