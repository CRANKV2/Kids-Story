package com.gigo.kidsstorys.ui.screens


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.data.models.Story
import com.gigo.kidsstorys.ui.components.AddStoryDialog
import com.gigo.kidsstorys.ui.components.StoryOptionsDialog
import com.gigo.kidsstorys.ui.components.story.CompactStoryLayout
import com.gigo.kidsstorys.ui.components.story.EmptyStateView
import com.gigo.kidsstorys.ui.components.story.GridStoryLayout
import com.gigo.kidsstorys.ui.components.story.StoryDeleteDialog
import com.gigo.kidsstorys.ui.components.story.StoryTopBar
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import java.io.File

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
    val backgroundImageFile = remember {
        File(localContext.filesDir, "background_image.jpg")
    }
    var backgroundBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var backgroundAlpha by remember { mutableStateOf(settingsManager.backgroundAlpha) }

    LaunchedEffect(Unit) {
        settingsManager.backgroundAlphaFlow.collect { newAlpha ->
            backgroundAlpha = newAlpha
        }
    }

    LaunchedEffect(Unit) {
        if (backgroundImageFile.exists()) {
            backgroundBitmap = BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundBitmap != null) {
            Image(
                bitmap = backgroundBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = backgroundAlpha
            )
        }

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
                    modifier = Modifier.padding(24.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.fab_add_icon),  // Stelle sicher, dass du eine ic_add.xml in deinen Ressourcen hast
                        contentDescription = "Geschichte hinzufügen",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (backgroundImageFile.exists()) {
                    val bitmap = remember {
                        BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
                    }
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = backgroundAlpha
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                            onDismiss = { selectedStory = null },
                            onEdit = {
                                navController.navigate("readStory/${story.id}")
                            },
                            onDelete = {
                                storyToDelete = story
                                showDeleteDialog = true
                                selectedStory = null
                            }
                        )
                    }

                    if (showDeleteDialog && storyToDelete != null) {
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
                            }
                        )
                    }
                }
            }
        }
    }
}