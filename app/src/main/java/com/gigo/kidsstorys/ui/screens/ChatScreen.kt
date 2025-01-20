package com.gigo.kidsstorys.ui.screens

import com.gigo.kidsstorys.ui.components.LoadingDots
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.data.models.ChatMessage
import com.gigo.kidsstorys.ui.components.StoryCategoryDropdown
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.viewmodels.ChatViewModel
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.utils.ImageUtils
import java.io.File

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
    storyViewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory),
    navController: NavController
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    // Hintergrundbild-File
    val backgroundImageFile = remember {
        File(context.filesDir, "background_image.jpg")
    }

    var backgroundBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val backgroundAlpha = remember { settingsManager.backgroundAlpha }

    // Initialisiere das Bitmap
    LaunchedEffect(Unit) {
        if (backgroundImageFile.exists()) {
            backgroundBitmap = BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hintergrundbild oder Fallback
        if (backgroundBitmap != null) {
            Image(
                bitmap = backgroundBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = backgroundAlpha
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimaryContainer) // Verwendet AMOLED Schwarz
            )
        }

        // Existierender Chat-Content
        Scaffold(
            containerColor = Color.Transparent,  // Macht Scaffold transparent
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(
                            elevation = 8.dp * cardAlpha.value,
                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    color = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .size(40.dp),
                            content = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(4.dp, CircleShape, spotColor = AccentPurple)
                                        .background(AccentPurple, CircleShape)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_arrow_back),
                                        contentDescription = "Zurück",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        )

                        Text(
                            "✨ DeMa ✨",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )

                        IconButton(
                            onClick = { viewModel.clearChat() },
                            modifier = Modifier
                                .size(40.dp),
                            content = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(4.dp, CircleShape, spotColor = AccentPurple)
                                        .background(AccentPurple, CircleShape)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Chat löschen",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding() // Wichtig für Keyboard-Handling
            ) {
                if (messages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (backgroundImageFile.exists()) {
                            // Nur Card anzeigen wenn Hintergrundbild existiert
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 8.dp,
                                        spotColor = AccentPurple.copy(alpha = 0.85f),
                                        ambientColor = AccentPurple.copy(alpha = 0.85f),
                                        shape = RoundedCornerShape(24.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF2D2D3A).copy(alpha = 0.95f)
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                WelcomeContent()
                            }
                        } else {
                            // Direkt Content ohne Card wenn kein Hintergrundbild
                            WelcomeContent()
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        var currentExamples by remember { mutableStateOf(listOf<String>()) }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            StoryCategoryDropdown(
                                onCategorySelected = { examples ->
                                    currentExamples = examples
                                },
                                hasBackground = backgroundImageFile.exists()
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            MessageCarousel(
                                examples = currentExamples,
                                onMessageSelected = { selectedMessage ->
                                    messageText = selectedMessage
                                }
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 115.dp)
                            .windowInsetsPadding(WindowInsets.ime) // Keyboard-Anpassung
                    ) {
                        items(messages) { message ->
                            ChatMessageItem(
                                message = message,
                                onSaveStory = { title, content, imagePath ->
                                    storyViewModel.addStory(
                                        title = title,
                                        content = content,
                                        imagePath = imagePath
                                    )
                                    Toast.makeText(
                                        context,
                                        "Geschichte wurde gespeichert!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        if (isLoading) {
                            item {
                                LoadingDots()
                            }
                        }
                    }
                }

                // Error Anzeige
                error?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(16.dp)
                                .shadow(
                                    elevation = 4.dp * cardAlpha.value,
                                    spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            color = Color(0xFF42424E).copy(alpha = cardAlpha.value),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(16.dp),
                                color = Color.White
                            )
                        }
                    }
                }

                // Chat Input Bar - jetzt am unteren Rand fixiert
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(20.dp)
                        .shadow(
                            elevation = 16.dp * cardAlpha.value,
                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    color = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 40.dp),
                            placeholder = {
                                Text(
                                    "Schreibe eine Nachricht...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 1.0f)
                            )
                        )
                        
                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    viewModel.sendMessage(messageText)
                                    messageText = ""
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            },
                            modifier = Modifier
                                .size(40.dp),
                            enabled = messageText.isNotBlank()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shadow(4.dp, CircleShape, spotColor = AccentPurple)
                                    .background(
                                        if (messageText.isNotBlank()) AccentPurple else AccentPurple.copy(alpha = 0.5f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Senden",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onSaveStory: (String, String, String?) -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    val isUserMessage = message.isUser
    var showPopup by remember { mutableStateOf(false) }
    var showTitleDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var selectedImagePath by remember { mutableStateOf<String?>(null) }

    val backgroundColor = if (isUserMessage) {
        Color(0xFF353545).copy(alpha = cardAlpha.value)
    } else {
        Color(0xFF42424E).copy(alpha = cardAlpha.value)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val tempFile = File(context.filesDir, "temp_story_image_${System.currentTimeMillis()}.jpg")
            ImageUtils.processAndSaveImage(context, uri, tempFile)
            selectedImagePath = tempFile.absolutePath
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isUserMessage) 32.dp else 4.dp,
                end = if (isUserMessage) 4.dp else 32.dp,
                top = 2.dp,
                bottom = 2.dp
            )
    ) {
        // Message-Box
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            modifier = Modifier
                .align(if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart)
                .shadow(
                    elevation = 4.dp * cardAlpha.value,
                    spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                    ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(enabled = !isUserMessage) { showPopup = true }
        ) {
            SelectionContainer {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { if (!isUserMessage) showPopup = true }
                            )
                        }
                ) {
                    Text(
                        text = message.content,
                        color = Color.White.copy(alpha = 1.0f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Erstes Popup (Speichern?)
        if (showPopup && !isUserMessage) {
            Dialog(onDismissRequest = { showPopup = false }) {
                Surface(
                    modifier = Modifier
                        .width(280.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF2D2D3A)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Geschichte speichern?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(onClick = { showPopup = false }) {
                                Text("Nein", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    showPopup = false
                                    showTitleDialog = true  // Öffne Titel-Dialog
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentPurple
                                )
                            ) {
                                Text("Ja")
                            }
                        }
                    }
                }
            }
        }

        // Zweites Popup (Titel eingeben)
        if (showTitleDialog) {
            Dialog(onDismissRequest = { showTitleDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF2D2D3A)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Geschichte speichern",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Bildauswahl-Button
                        FilledTonalButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentPurple
                            )
                        ) {
                            Text(
                                if (selectedImagePath == null) "Bild hinzufügen" else "Bild ändern",
                                color = Color.White
                            )
                        }

                        // Bildvorschau
                        if (selectedImagePath != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(bottom = 16.dp)
                            ) {
                                val bitmap = remember(selectedImagePath) {
                                    BitmapFactory.decodeFile(selectedImagePath)
                                }
                                bitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { imagePickerLauncher.launch("image/*") },
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Titel eingeben", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentPurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = AccentPurple,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = AccentPurple
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { 
                                    showTitleDialog = false
                                    title = ""
                                    selectedImagePath = null
                                }
                            ) {
                                Text("Abbrechen", color = Color.White)
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Button(
                                onClick = {
                                    if (title.isNotBlank()) {
                                        onSaveStory(title, message.content, selectedImagePath)
                                        showTitleDialog = false
                                        title = ""
                                        selectedImagePath = null
                                    }
                                },
                                enabled = title.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentPurple
                                )
                            ) {
                                Text("Speichern")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MessageCarousel(
    examples: List<String>,
    onMessageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)  // Padding für erste/letzte Karte
    ) {
        items(examples) { example ->
            Card(
                modifier = Modifier
                    .width(280.dp)  // Feste Breite für die Karten
                    .height(140.dp)
                    .clickable { onMessageSelected(example) }
                    .shadow(
                        elevation = 4.dp * cardAlpha.value,
                        spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                        ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                        shape = RoundedCornerShape(10.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = example,
                        modifier = Modifier.padding(10.dp),
                        color = Color.White,
                        fontSize = 16.sp,
                        lineHeight = 19.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Ausgelagerte Welcome-Content Composable
@Composable
private fun WelcomeContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Hallo!\nIch bin DeMa 😊\n\nDein\nKI-Geschichtenhelfer!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            "\nErzähl mir, was für eine Geschichte du erschaffen möchtest!\n\nIch helfe dir dabei, magische Abenteuer zu gestalten. ✨",
            color = Color.White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Zum Beispiel:",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}