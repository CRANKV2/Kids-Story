package com.gigo.kidsstorys.ui.screens


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import java.io.File

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
    var showCreateOptions by remember { mutableStateOf(false) }

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
                Box(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    FloatingActionButton(
                        onClick = { showCreateOptions = true },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fab_add_icon),
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

            if (showCreateOptions) {
                Dialog(onDismissRequest = { showCreateOptions = false }) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()  // Macht den Dialog etwas schmaler
                                .padding(5.dp),
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF2D2D3A).copy(alpha = 1.0f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    "Geschichte erstellen",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextLight,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Button(
                                    onClick = {
                                        showCreateOptions = false
                                        showAddDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth(0.8f),  // Buttons etwas schmaler
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentPurple.copy(alpha = 1.0f)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        "Selbst schreiben",
                                        color = Color.White,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Text(
                                    "Oder",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextLight,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Button(
                                    onClick = {
                                        showCreateOptions = false
                                        navController.navigate("chat")
                                    },
                                    modifier = Modifier.fillMaxWidth(0.8f),  // Buttons etwas schmaler
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentPurple.copy(alpha = 1.0f)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        "Mit DeMa KI-Hilfe erstellen",
                                        color = Color.White,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }




        }
    }
}