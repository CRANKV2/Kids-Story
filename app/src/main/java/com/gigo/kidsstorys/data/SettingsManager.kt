package com.gigo.kidsstorys.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val dataStore = context.dataStore
    private val cARDALPHA = floatPreferencesKey("card_alpha")
    private val _backgroundAlphaFlow = MutableStateFlow(DEFAULT_BACKGROUND_ALPHA)
    val backgroundAlphaFlow: StateFlow<Float> = _backgroundAlphaFlow

    companion object {
        private const val PREFS_NAME = "story_settings"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_WRAP_TEXT = "wrap_text"
        private const val KEY_TITLE_SIZE = "title_size"
        private const val KEY_PREVIEW_SIZE = "preview_size"
        private const val KEY_COMPACT_VIEW = "compact_view"
        private const val KEY_BACKGROUND_ALPHA = "background_alpha"
        
        private const val DEFAULT_CARD_ALPHA = 1.0f
        private const val DEFAULT_BACKGROUND_ALPHA = 1.0f
        private const val DEFAULT_FONT_SIZE = 14
        private const val DEFAULT_WRAP_TEXT = true
        private const val DEFAULT_TITLE_SIZE = 15
        private const val DEFAULT_PREVIEW_SIZE = 12

        @Volatile
        private var instance: SettingsManager? = null

        fun getInstance(context: Context): SettingsManager {
            return instance ?: synchronized(this) {
                instance ?: SettingsManager(context).also { instance = it }
            }
        }
    }

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

    var backgroundAlpha: Float
        get() = sharedPreferences.getFloat(KEY_BACKGROUND_ALPHA, DEFAULT_BACKGROUND_ALPHA)
        set(value) {
            sharedPreferences.edit().putFloat(KEY_BACKGROUND_ALPHA, value).apply()
            _backgroundAlphaFlow.value = value
        }

    val cardAlpha: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[cARDALPHA] ?: DEFAULT_CARD_ALPHA
        }

    fun updateTitleSize(size: Int) {
        titleSize = size
    }

    fun updatePreviewSize(size: Int) {
        previewSize = size
    }

    suspend fun updateCardAlpha(alpha: Float) {
        dataStore.edit { preferences ->
            preferences[cARDALPHA] = alpha
        }
    }
}