package com.gigo.kidsstorys.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gigo.kidsstorys.data.Story
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.ui.components.*
import com.gigo.kidsstorys.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager

@OptIn(ExperimentalMaterial3Api::class)
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

    Log.d("StoryScreen", "CardTitleColor: ${userPreferences.cardTitleColor}")
    Log.d("StoryScreen", "CardPreviewColor: ${userPreferences.cardPreviewColor}")

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Blur-Hintergrund
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = AccentPurple.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .blur(radius = 20.dp)
                )
                
                // Inhalt
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Geschichten",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "${stories.size} Geschichten",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { showTutorial = true }
                            ) {
                                Text(stringResource(R.string.tipps_info_emoji), fontSize = 24.sp)
                            }
                            
                            IconButton(
                                onClick = { navController.navigate("settings") }
                            ) {
                                Text(stringResource(R.string.settings_emoji), fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AccentPurple,
                shape = CircleShape
            ) {
                Text(stringResource(R.string.fab_button_inner), fontSize = 24.sp, color = Color.White)
            }
        },
        containerColor = if (isDarkTheme) Color(0xFF1E1E2E) else Color(0xFFF5F5F5)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (stories.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "📚",
                        fontSize = 48.sp,
                        color = Color(0xFF9575CD).copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Keine Geschichten vorhanden",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFE2E2E2).copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tippe auf + um eine neue Geschichte zu erstellen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFBBBBBB).copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(stories) { story ->
                        val titleColor = Color(userPreferences.cardTitleColor.toInt())
                        val previewColor = Color(userPreferences.cardPreviewColor.toInt())
                        
                        StoryCard(
                            story = story,
                            onCardClick = { navController.navigate("readStory/${story.id}") },
                            onOptionsClick = { selectedStory = story },
                            titleColor = titleColor,
                            previewColor = previewColor,
                            previewSize = settingsManager.previewSize
                        )
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