package com.gigo.kidsstorys.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class UserPreferences(
    val fontSize: Float = 16f,
    val wrapText: Boolean = true,
    val cardTitleColor: Long = 0xFFE2E2E2,
    val cardPreviewColor: Long = 0xFFBBBBBB,
    val storyTitleColor: Long = 0xFFE2E2E2,
    val storyTextColor: Long = 0xFFE2E2E2
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    // Bestehende Companion Object Keys
    companion object {
        val FONT_SIZE = floatPreferencesKey("font_size")
        val WRAP_TEXT = booleanPreferencesKey("wrap_text")
        // Neue Keys für Farben
        val CARD_TITLE_COLOR = longPreferencesKey("card_title_color")
        val CARD_PREVIEW_COLOR = longPreferencesKey("card_preview_color")
        val STORY_TITLE_COLOR = longPreferencesKey("story_title_color")
        val STORY_TEXT_COLOR = longPreferencesKey("story_text_color")
    }

    // Bestehende Funktionen bleiben unverändert

    // Neue Funktionen für Farbeinstellungen
    suspend fun updateCardTitleColor(color: Color) {
        dataStore.edit { preferences ->
            preferences[CARD_TITLE_COLOR] = color.toArgb().toLong()
        }
    }

    suspend fun updateCardPreviewColor(color: Color) {
        dataStore.edit { preferences ->
            preferences[CARD_PREVIEW_COLOR] = color.toArgb().toLong()
        }
    }

    suspend fun updateStoryTitleColor(color: Color) {
        dataStore.edit { preferences ->
            preferences[STORY_TITLE_COLOR] = color.toArgb().toLong()
        }
    }

    suspend fun updateStoryTextColor(color: Color) {
        dataStore.edit { preferences ->
            preferences[STORY_TEXT_COLOR] = color.toArgb().toLong()
        }
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            fontSize = preferences[FONT_SIZE] ?: 16f,
            wrapText = preferences[WRAP_TEXT] ?: true,
            cardTitleColor = preferences[CARD_TITLE_COLOR] ?: 0xFFE2E2E2,
            cardPreviewColor = preferences[CARD_PREVIEW_COLOR] ?: 0xFFBBBBBB,
            storyTitleColor = preferences[STORY_TITLE_COLOR] ?: 0xFFE2E2E2,
            storyTextColor = preferences[STORY_TEXT_COLOR] ?: 0xFFE2E2E2
        )
    }
} 