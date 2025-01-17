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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
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
    var selectedStories by remember { mutableStateOf(setOf<Int>()) }
    var isSelectionMode by remember { mutableStateOf(false) }

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
                if (isSelectionMode) {
                    TopAppBar(
                        title = {
                            Text(
                                "${selectedStories.size} ausgewählt",
                                color = Color.White
                            )
                        },
                        actions = {
                            TextButton(
                                onClick = {
                                    selectedStories = if (selectedStories.size == stories.size) {
                                        emptySet()
                                    } else {
                                        stories.map { it.id }.toSet()
                                    }
                                }
                            ) {
                                Text(
                                    if (selectedStories.size == stories.size) "Alle abwählen" else "Alle auswählen",
                                    color = Color.White
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.deleteStories(selectedStories.toList())
                                    isSelectionMode = false
                                    selectedStories = emptySet()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Löschen",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = {
                                    isSelectionMode = false
                                    selectedStories = emptySet()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Schließen",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                } else {
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
                }
            },


            floatingActionButton = {
                if (!isSelectionMode) {
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
                                onStoryClick = { story -> 
                                    if (isSelectionMode) {
                                        selectedStories = if (selectedStories.contains(story.id)) {
                                            selectedStories - story.id
                                        } else {
                                            selectedStories + story.id
                                        }
                                    } else {
                                        navController.navigate("readStory/${story.id}")
                                    }
                                },
                                onLongClick = { story ->
                                    if (!isSelectionMode) {
                                        isSelectionMode = true
                                        selectedStories = setOf(story.id)
                                    }
                                },
                                onOptionsClick = { story -> 
                                    if (!isSelectionMode) {
                                        selectedStory = story
                                    }
                                }
                            )
                        } else {
                            CompactStoryLayout(
                                stories = stories,
                                userPreferences = userPreferences,
                                isCompactView = isCompactView,
                                onStoryClick = { story -> 
                                    if (isSelectionMode) {
                                        selectedStories = if (selectedStories.contains(story.id)) {
                                            selectedStories - story.id
                                        } else {
                                            selectedStories + story.id
                                        }
                                    } else {
                                        navController.navigate("readStory/${story.id}")
                                    }
                                },
                                onLongClick = { story ->
                                    if (!isSelectionMode) {
                                        isSelectionMode = true
                                        selectedStories = setOf(story.id)
                                    }
                                },
                                onOptionsClick = { story -> selectedStory = story }
                            )
                        }
                    }

                    if (showAddDialog) {
                        AddStoryDialog(
                            onDismissRequest = { showAddDialog = false },
                            onConfirm = { title, content, imagePath ->
                                viewModel.addStory(title, content, imagePath)
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