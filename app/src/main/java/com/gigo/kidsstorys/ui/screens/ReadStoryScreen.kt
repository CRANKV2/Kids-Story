package com.gigo.kidsstorys.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.ui.theme.*
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.data.SettingsManager
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.gigo.kidsstorys.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadStoryScreen(
    storyId: Int,
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    viewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory)
) {
    val story by viewModel.selectedStory.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showContentEditDialog by remember { mutableStateOf(false) }
    var showTitleEditDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    var fontSize by remember { mutableStateOf(userPreferences.fontSize.toFloat()) }
    val wrapText = settingsManager.wrapText
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    
    // Farben aus den Preferences
    val titleColor = remember(userPreferences.storyTitleColor) {
        Color(userPreferences.storyTitleColor.toInt())
    }
    val textColor = remember(userPreferences.storyTextColor) {
        Color(userPreferences.storyTextColor.toInt())
    }

    LaunchedEffect(storyId) {
        viewModel.loadStory(storyId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        story?.let { currentStory ->
            var currentTitle by remember { mutableStateOf(currentStory.title) }
            var currentContent by remember { mutableStateOf(currentStory.content) }
            
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Text(stringResource(R.string.navigation_left), fontSize = 24.sp)
                            }
                        },
                        actions = {
                            IconButton(onClick = { showEditDialog = true }) {
                                Text(stringResource(R.string.aendern), fontSize = 24.sp)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF1E1E2A)
                        )
                    )
                },
                containerColor = Color(0xFF1E1E2A)
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Titel-Sektion
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2D2D3A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = currentTitle,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(userPreferences.storyTitleColor)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AssistChip(
                                onClick = { },
                                label = { Text(stringResource(R.string.read_story_screen_geschichte)) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF353545),
                                    labelColor = Color(0xFF9575CD)
                                )
                            )
                        }
                    }

                    // Geschichte-Sektion
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2D2D3A)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .verticalScroll(verticalScrollState)
                        ) {
                            Text(
                                text = story?.title ?: "",
                                style = MaterialTheme.typography.headlineMedium,
                                color = titleColor,
                                fontSize = fontSize.sp
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (wrapText) {
                                Text(
                                    text = story?.content ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = textColor,
                                    maxLines = Int.MAX_VALUE,
                                    overflow = TextOverflow.Visible,
                                    modifier = Modifier.pointerInput(Unit) {
                                        detectTransformGestures { _, _, zoom, _ ->
                                            fontSize = (fontSize * zoom).coerceIn(12f, 30f)
                                        }
                                    }
                                )
                            } else {
                                // Horizontal Scroll für den Text
                                Row(
                                    modifier = Modifier.horizontalScroll(horizontalScrollState)
                                ) {
                                    Text(
                                        text = story?.content ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = textColor,
                                        overflow = TextOverflow.Visible,
                                        modifier = Modifier.pointerInput(Unit) {
                                            detectTransformGestures { _, _, zoom, _ ->
                                                fontSize = (fontSize * zoom).coerceIn(12f, 30f)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Existierende Dialoge bleiben unverändert
                if (showEditDialog) {
                    AlertDialog(
                        onDismissRequest = { showEditDialog = false },
                        title = { 
                            Text(
                                "Was möchtest du bearbeiten?",
                                color = TextLight
                            )
                        },
                        containerColor = if (isDarkTheme) CardDark else CardLight,
                        confirmButton = {},
                        dismissButton = {
                            TextButton(onClick = { showEditDialog = false }) {
                                Text(stringResource(R.string.abbrechen), color = TextLight)
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { 
                                        showEditDialog = false
                                        showContentEditDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentPurple
                                    )
                                ) {
                                    Text(stringResource(R.string.geschichte_bearbeiten))
                                }
                                Button(
                                    onClick = { 
                                        showEditDialog = false
                                        showTitleEditDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentPurple
                                    )
                                ) {
                                    Text(stringResource(R.string.titel_bearbeiten))
                                }
                            }
                        }
                    )
                }

                // Dialog für die Bearbeitung des Inhalts
                if (showContentEditDialog) {
                    AlertDialog(
                        onDismissRequest = { showContentEditDialog = false },
                        title = { Text(stringResource(R.string.geschichte_bearbeiten), color = TextLight) },
                        containerColor = if (isDarkTheme) CardDark else CardLight,
                        text = {
                            OutlinedTextField(
                                value = currentContent,
                                onValueChange = { currentContent = it },
                                label = { Text(stringResource(R.string.geschichte), color = TextLight) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TextLight,
                                    unfocusedBorderColor = TextLight.copy(alpha = 0.5f),
                                    focusedLabelColor = TextLight,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 5
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.updateStory(currentStory.copy(content = currentContent))
                                    showContentEditDialog = false
                                }
                            ) {
                                Text(stringResource(R.string.speichern))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showContentEditDialog = false }) {
                                Text(stringResource(R.string.abbrechen), color = TextLight)
                            }
                        }
                    )
                }

                // Existierender Dialog für die Titel-Bearbeitung bleibt unverändert
                if (showTitleEditDialog) {
                    var newTitle by remember { mutableStateOf(currentTitle) }
                    
                    Dialog(onDismissRequest = { showTitleEditDialog = false }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isDarkTheme) CardDark else CardLight
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    stringResource(R.string.titel_bearbeiten),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextLight
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = newTitle,
                                    onValueChange = { newTitle = it },
                                    label = { Text(stringResource(R.string.titel)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TextLight,
                                        unfocusedBorderColor = TextLight.copy(alpha = 0.5f),
                                        focusedLabelColor = TextLight,
                                        unfocusedLabelColor = TextLight.copy(alpha = 0.5f)
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = { showTitleEditDialog = false }
                                    ) {
                                        Text(stringResource(R.string.abbrechen), color = TextLight)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            story?.let { currentStory ->
                                                viewModel.updateStoryTitle(currentStory, newTitle)
                                            }
                                            showTitleEditDialog = false
                                        }
                                    ) {
                                        Text(stringResource(R.string.speichern))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 