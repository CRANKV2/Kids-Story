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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.gigo.kidsstorys.data.Story
import com.gigo.kidsstorys.ui.theme.*
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.ui.components.*
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoryScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    viewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory)
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val stories by viewModel.allStories.collectAsState(initial = emptyList())
    val userPreferences by viewModel.userPreferences.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var storyToDelete by remember { mutableStateOf<Story?>(null) }
    var showTutorial by remember { mutableStateOf(false) }
    
    // Verwende den gespeicherten Wert als Initial-State
    var isCompactView by remember { mutableStateOf(settingsManager.isCompactView) }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 16.dp,
                        spotColor = AccentPurple,
                        ambientColor = AccentPurple,
                        shape = RoundedCornerShape(24.dp)
                    ),
                color = Color(0xFF2D2D3A),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Geschichten",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "${stories.size} Geschichten",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Neuer Button zum Umschalten der Ansicht
                        IconButton(
                            onClick = { 
                                isCompactView = !isCompactView
                                settingsManager.isCompactView = isCompactView  // Speichere die neue Einstellung
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, CircleShape, spotColor = AccentPurple)
                                .background(Color(0xFF353545), CircleShape)
                        ) {
                            Text(
                                if (isCompactView) "⋮⋮" else "⋮", 
                                fontSize = 24.sp,
                                color = TextLight
                            )
                        }

                        IconButton(
                            onClick = { showTutorial = true },
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, CircleShape, spotColor = AccentPurple)
                                .background(Color(0xFF353545), CircleShape)
                        ) {
                            Text("❓", fontSize = 24.sp)
                        }
                        
                        IconButton(
                            onClick = { navController.navigate("settings") },
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, CircleShape, spotColor = AccentPurple)
                                .background(Color(0xFF353545), CircleShape)
                        ) {
                            Text("⚙️", fontSize = 24.sp)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = CircleShape,
                        spotColor = AccentPurple,
                        ambientColor = AccentPurple
                    ),
                containerColor = AccentPurple,
                shape = CircleShape
            ) {
                Text("+", fontSize = 32.sp, color = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (stories.isEmpty()) {
                EmptyStateView()
            } else {
                if (!isCompactView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(
                            items = stories,
                            key = { it.id }
                        ) { story ->
                            val titleColor = Color(userPreferences.cardTitleColor.toInt())
                            val previewColor = Color(userPreferences.cardPreviewColor.toInt())
                            
                            Box(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .fillMaxWidth(0.5f)
                            ) {
                                ModernStoryCard(
                                    story = story,
                                    onCardClick = { navController.navigate("readStory/${story.id}") },
                                    onOptionsClick = { selectedStory = story },
                                    titleColor = titleColor,
                                    previewColor = previewColor,
                                    previewSize = settingsManager.previewSize,
                                    isCompactView = isCompactView,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                } else {
                    // Einzelne Spalte für die Preview-Ansicht
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(stories) { story ->
                            val titleColor = Color(userPreferences.cardTitleColor.toInt())
                            val previewColor = Color(userPreferences.cardPreviewColor.toInt())
                            
                            ModernStoryCard(
                                story = story,
                                onCardClick = { navController.navigate("readStory/${story.id}") },
                                onOptionsClick = { selectedStory = story },
                                titleColor = titleColor,
                                previewColor = previewColor,
                                previewSize = settingsManager.previewSize,
                                isCompactView = isCompactView
                            )
                        }
                    }
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

        if (showDeleteDialog && storyToDelete != null) {
            AlertDialog(
                onDismissRequest = { 
                    showDeleteDialog = false
                    storyToDelete = null
                },
                title = { Text(stringResource(R.string.geschichte_loeschen), color = TextLight) },
                text = { 
                    Text(
                        stringResource(R.string.frage_wirklich_loeschen),
                        color = TextLight
                    ) 
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            storyToDelete?.let { story ->
                                viewModel.deleteStory(story)
                            }
                            showDeleteDialog = false
                            storyToDelete = null
                        }
                    ) {
                        Text(stringResource(R.string.loeschen), color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { 
                            showDeleteDialog = false
                            storyToDelete = null
                        }
                    ) {
                        Text(stringResource(R.string.abbrechen), color = TextLight)
                    }
                },
                containerColor = if (isDarkTheme) CardDark else CardLight
            )
        }
    }

    if (showTutorial) {
        var currentTipIndex by remember { mutableStateOf(0) }
        val tips = listOf(
            "Tippe auf + um eine neue Geschichte zu erstellen",
            "Tippe auf eine Geschichte um sie zu lesen",
            "Tippe auf ⚙️ um die Einstellungen zu öffnen",
            "Tippe auf ⋮ um eine Geschichte zu bearbeiten oder zu löschen",
            "In den Einstellungen kannst du Textgrößen und Farben anpassen"
        )

        Dialog(
            onDismissRequest = { showTutorial = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color(0xFF9575CD)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF2D2D3A) else Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Titel
                    Text(
                        stringResource(R.string.tipps_tricks),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLight
                    )

                    // Tipps Text
                    Text(
                        text = tips[currentTipIndex],
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = TextLight,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    
                    // Navigation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { 
                                if (currentTipIndex > 0) currentTipIndex-- 
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = if (isDarkTheme) Color(0xFF353545) else Color(0xFFEEEEEE),
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                stringResource(R.string.tipps_tricks_back),
                                fontSize = 32.sp
                            )
                        }
                        
                        Text(
                            "${currentTipIndex + 1}/${tips.size}",
                            fontSize = 18.sp,
                            color = TextLight
                        )
                        
                        IconButton(
                            onClick = { 
                                if (currentTipIndex < tips.size - 1) currentTipIndex++ 
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = if (isDarkTheme) Color(0xFF353545) else Color(0xFFEEEEEE),
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                stringResource(R.string.tipps_tricks_forward),
                                fontSize = 32.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Verstanden Button
                    Button(
                        onClick = { showTutorial = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentPurple
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(50.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = AccentPurple
                            )
                    ) {
                        Text(
                            stringResource(R.string.verstanden),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = AccentPurple.copy(alpha = 0.5f)
                ),
            color = Color(0xFF2D2D3A),
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "📚",
                    fontSize = 48.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Keine Geschichten vorhanden",
            style = MaterialTheme.typography.titleLarge,
            color = TextLight,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tippe auf + um eine neue Geschichte zu erstellen",
            style = MaterialTheme.typography.bodyLarge,
            color = TextLight.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ModernStoryCard(
    story: Story,
    onCardClick: () -> Unit,
    onOptionsClick: () -> Unit,
    titleColor: Color,
    previewColor: Color,
    previewSize: Int,
    isCompactView: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = AccentPurple.copy(alpha = 0.5f),
                ambientColor = AccentPurple.copy(alpha = 0.3f)
            ),
        color = Color(0xFF2D2D3A),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (isCompactView) 20.dp else 16.dp),
            horizontalAlignment = if (!isCompactView) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (!isCompactView) Arrangement.Center else Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = story.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (!isCompactView) 24.sp else 22.sp
                    ),
                    color = titleColor,
                    modifier = if (isCompactView) Modifier.weight(1f) else Modifier,
                    textAlign = if (!isCompactView) TextAlign.Center else TextAlign.Start
                )
                
                if (isCompactView) {
                    IconButton(
                        onClick = onOptionsClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF353545), CircleShape)
                    ) {
                        Text("⋮", fontSize = 24.sp, color = TextLight)
                    }
                }
            }
            
            if (isCompactView) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = story.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = previewColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = previewSize.sp,
                    lineHeight = (previewSize * 1.5).sp
                )
            }
            
            if (!isCompactView) {
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = onOptionsClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF353545), CircleShape)
                ) {
                    Text("⋮", fontSize = 24.sp, color = TextLight)
                }
            }
        }
    }
}