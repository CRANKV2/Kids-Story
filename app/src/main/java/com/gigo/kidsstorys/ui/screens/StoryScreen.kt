package com.gigo.kidsstorys.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gigo.kidsstorys.data.models.Story
import com.gigo.kidsstorys.ui.theme.*
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.ui.components.*
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.components.story.ModernStoryCard
import com.gigo.kidsstorys.ui.components.story.EmptyStateView
import com.gigo.kidsstorys.ui.components.story.StoryTutorialDialog
import com.gigo.kidsstorys.ui.components.story.StoryDeleteDialog
import com.gigo.kidsstorys.ui.components.story.StoryTopBar
import com.gigo.kidsstorys.ui.components.story.GridStoryLayout
import com.gigo.kidsstorys.ui.components.story.CompactStoryLayout

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoryScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    viewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory)
) {
    val localContext = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(localContext) }
    val stories by viewModel.allStories.collectAsState(initial = emptyList())
    val userPreferences by viewModel.userPreferences.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var storyToDelete by remember { mutableStateOf<Story?>(null) }
    var showTutorial by remember { mutableStateOf(false) }
    var isCompactView by remember { mutableStateOf(settingsManager.isCompactView) }

    Scaffold(
        topBar = {
            StoryTopBar(
                storiesCount = stories.size,
                isCompactView = isCompactView,
                onViewToggle = { 
                    isCompactView = !isCompactView
                    settingsManager.isCompactView = isCompactView
                },
                onChatClick = { navController.navigate("chat") },
                onSettingsClick = { navController.navigate("settings") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text("+", fontSize = 24.sp)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (stories.isEmpty()) {
                EmptyStateView()
            } else {
                if (!isCompactView) {
                    GridStoryLayout(
                        stories = stories,
                        userPreferences = userPreferences,
                        isCompactView = isCompactView,
                        onStoryClick = { story -> navController.navigate("readStory/${story.id}") },
                        onOptionsClick = { story -> selectedStory = story }
                    )
                } else {
                    CompactStoryLayout(
                        stories = stories,
                        userPreferences = userPreferences,
                        isCompactView = isCompactView,
                        onStoryClick = { story -> navController.navigate("readStory/${story.id}") },
                        onOptionsClick = { story -> selectedStory = story }
                    )
                }
            }
        }

        if (showAddDialog) {
            AddStoryDialog(
                onDismissRequest = { showAddDialog = false },
                onConfirm = { title, content ->
                    viewModel.addStory(title, content)
                    showAddDialog = false
                }
            )
        }

        selectedStory?.let { story ->
            StoryOptionsDialog(
                story = story,
                onDismiss = { selectedStory = null },
                onEdit = { 
                    navController.navigate("readStory/${story.id}")
                },
                onDelete = {
                    storyToDelete = story
                    showDeleteDialog = true
                    selectedStory = null
                },
                isDarkTheme = isDarkTheme
            )
        }

        if (showDeleteDialog) {
            StoryDeleteDialog(
                onConfirm = {
                    storyToDelete?.let { story ->
                        viewModel.deleteStory(story)
                    }
                    showDeleteDialog = false
                    storyToDelete = null
                },
                onDismiss = {
                    showDeleteDialog = false
                    storyToDelete = null
                },
                isDarkTheme = isDarkTheme
            )
        }

        if (showTutorial) {
            StoryTutorialDialog(
                onDismiss = { showTutorial = false },
                isDarkTheme = isDarkTheme
            )
        }
    }
}