package com.gigo.storyflow.ui.components.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gigo.storyflow.data.SettingsManager
import com.gigo.storyflow.data.UserPreferences
import com.gigo.storyflow.data.models.Story

@Composable
fun GridStoryLayout(
    stories: List<Story>,
    userPreferences: UserPreferences,
    onStoryClick: (Story) -> Unit,
    onOptionsClick: (Story) -> Unit,
    isSelectionMode: Boolean = false,
    selectedStories: Set<Int> = emptySet(),
    onLongClick: (Story) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val titleSize = SettingsManager.getInstance(LocalContext.current).titleSize
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Linke Spalte
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stories
                    .filterIndexed { index, _ -> index % 2 == 0 }
                    .forEach { story ->
                        val titleColor = Color(userPreferences.cardTitleColor.toInt())
                        val previewColor = Color(userPreferences.cardPreviewColor.toInt())
                        
                        ModernStoryCard(
                            story = story,
                            onCardClick = { onStoryClick(story) },
                            onOptionsClick = { 
                                if (!isSelectionMode) onOptionsClick(story)
                            },
                            onLongClick = { onLongClick(story) },
                            titleColor = titleColor,
                            previewColor = previewColor,
                            previewSize = SettingsManager.getInstance(LocalContext.current).previewSize,
                            titleSize = titleSize,
                            isSelected = selectedStories.contains(story.id),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }

            // Rechte Spalte
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stories
                    .filterIndexed { index, _ -> index % 2 == 1 }
                    .forEach { story ->
                        val titleColor = Color(userPreferences.cardTitleColor.toInt())
                        val previewColor = Color(userPreferences.cardPreviewColor.toInt())
                        
                        ModernStoryCard(
                            story = story,
                            onCardClick = { onStoryClick(story) },
                            onOptionsClick = { 
                                if (!isSelectionMode) onOptionsClick(story)
                            },
                            onLongClick = { onLongClick(story) },
                            titleColor = titleColor,
                            previewColor = previewColor,
                            previewSize = SettingsManager.getInstance(LocalContext.current).previewSize,
                            titleSize = titleSize,
                            isSelected = selectedStories.contains(story.id),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun CompactStoryLayout(
    stories: List<Story>,
    userPreferences: UserPreferences,
    onStoryClick: (Story) -> Unit,
    onOptionsClick: (Story) -> Unit,
    isSelectionMode: Boolean = false,
    selectedStories: Set<Int> = emptySet(),
    onLongClick: (Story) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(stories) { story ->
            val titleColor = Color(userPreferences.cardTitleColor.toInt())
            val previewColor = Color(userPreferences.cardPreviewColor.toInt())
            val titleSize = SettingsManager.getInstance(LocalContext.current).titleSize
            
            ModernStoryCard(
                story = story,
                onCardClick = { onStoryClick(story) },
                onOptionsClick = { 
                    if (!isSelectionMode) onOptionsClick(story)
                },
                onLongClick = { onLongClick(story) },
                titleColor = titleColor,
                previewColor = previewColor,
                previewSize = SettingsManager.getInstance(LocalContext.current).previewSize,
                titleSize = titleSize,
                isSelected = selectedStories.contains(story.id),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 