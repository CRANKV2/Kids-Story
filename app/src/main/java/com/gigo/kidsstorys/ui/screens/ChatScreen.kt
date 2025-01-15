package com.gigo.kidsstorys.ui.screens

import LoadingDots
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.data.models.ChatMessage
import com.gigo.kidsstorys.ui.viewmodels.ChatViewModel
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.painterResource
import com.gigo.kidsstorys.R
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.gigo.kidsstorys.ui.theme.TextLight
import java.io.File
import com.gigo.kidsstorys.ui.components.StoryCategoryDropdown
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.gigo.kidsstorys.data.SettingsManager
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
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
    val coroutineScope = rememberCoroutineScope()
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
        // Hintergrundbild
        if (backgroundBitmap != null) {
            Image(
                bitmap = backgroundBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = backgroundAlpha
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
                        
                        Text(
                            "Hallo! \nIch bin DeMa \uD83D\uDE0A \n\nDein\nKI-Geschichtenhelfer!",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            "Erzähl mir, was für eine Geschichte du erschaffen möchtest! \nIch helfe dir dabei, magische Abenteuer zu gestalten. ✨",
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            "Zum Beispiel:",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        var currentExamples by remember { mutableStateOf(listOf<String>()) }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            StoryCategoryDropdown(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp),
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
                                },
                                hasBackground = backgroundImageFile.exists()
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
                                onSaveStory = { title, content ->
                                    storyViewModel.addStory(
                                        title = title,
                                        content = content
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
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
fun ChatInputBox(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF2D2D3A),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF353545),
                    focusedContainerColor = Color(0xFF353545),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                ),
                placeholder = {
                    Text(
                        "Schreibe eine Nachricht...",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            )
            
            IconButton(
                onClick = onSendClick,
                enabled = messageText.isNotBlank()
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Senden",
                    tint = if (messageText.isNotBlank()) AccentPurple else Color.Gray
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onSaveStory: (String, String) -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    val isUserMessage = message.isUser
    var showPopup by remember { mutableStateOf(false) }
    var showTitleDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    val backgroundColor = if (isUserMessage) {
        Color(0xFF353545).copy(alpha = cardAlpha.value)
    } else {
        Color(0xFF42424E).copy(alpha = cardAlpha.value)
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
            Text(
                text = message.content,
                modifier = Modifier.padding(8.dp),
                color = Color.White.copy(alpha = maxOf(cardAlpha.value + 0.2f, 1f)),
                fontSize = 14.sp
            )
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
                            "Titel für die Geschichte",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                            TextButton(onClick = { 
                                showTitleDialog = false
                                title = ""
                            }) {
                                Text("Abbrechen", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (title.isNotBlank()) {
                                        onSaveStory(title, message.content)  // Hier übergeben wir beide Parameter
                                        showTitleDialog = false
                                        title = ""
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

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 

@Composable
fun ChatMessage(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isUserMessage = message.isUser
    val backgroundColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (isUserMessage) 40.dp else 8.dp,
                end = if (isUserMessage) 8.dp else 40.dp,
                top = 4.dp,
                bottom = 4.dp
            )
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            modifier = Modifier
                .align(if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart)
                .clickable { } // Macht die Box klickbar für Copy-Funktion
        ) {
            SelectionContainer { // Dies macht den Text selektierbar
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
} 

@Composable
fun MessageCarousel(
    examples: List<String>,
    onMessageSelected: (String) -> Unit,
    hasBackground: Boolean,
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