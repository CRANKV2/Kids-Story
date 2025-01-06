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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog

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
    // Effekt zum automatischen Scrollen
    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            delay(100) // Kleine Verzögerung für smoother Scroll
            listState.animateScrollToItem(index = messages.lastIndex)
        }
    }

    // Error Handling
    LaunchedEffect(error) {
        error?.let {
            // Optional: Snackbar oder Toast anzeigen
            viewModel.clearError()
        }
    }

    // WindowInsets für Keyboard-Anpassung
    val windowInsets = WindowInsets.ime
        .add(WindowInsets.navigationBars)
        .asPaddingValues()

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
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(8.dp, CircleShape, spotColor = AccentPurple)
                            .background(AccentPurple, CircleShape)
                    ) {
                        Text("◀️", fontSize = 24.sp)
                    }
                    Text(
                        "Story AI Chat",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    IconButton(
                        onClick = { viewModel.clearChat() },
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(8.dp, CircleShape, spotColor = AccentPurple)
                            .background(AccentPurple, CircleShape)
                    ) {
                        Text("🗑️", fontSize = 24.sp)
                    }
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
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "👋 Hallo! Ich bin dein\nKI-Geschichtenhelfer!",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Erzähl mir, was für eine Geschichte du erschaffen möchtest! Ich helfe dir dabei, magische Abenteuer zu gestalten. ✨",
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Zum Beispiel:",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Eine Geschichte über einen mutigen Drachen\n" +
                        "• Ein Abenteuer im Weltraum\n" +
                        "• Eine lustige Geschichte über sprechende Tiere",
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 80.dp)
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
                            .shadow(4.dp, RoundedCornerShape(8.dp)),
                        color = Color(0xFF42424E),
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

            // Chat Input Box
            ChatInputBox(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(messageText)
                        messageText = ""
                        keyboardController?.hide() // Keyboard verstecken nach Senden
                        focusManager.clearFocus() // Focus entfernen
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF2D2D3A))
                    .navigationBarsPadding() // Navigation Bar Padding
            )
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
                    Icons.Filled.Send,
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
    val isUserMessage = message.isUser
    var showPopup by remember { mutableStateOf(false) }
    var showTitleDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isUserMessage) 40.dp else 8.dp,
                end = if (isUserMessage) 8.dp else 40.dp,
                top = 4.dp,
                bottom = 4.dp
            )
    ) {
        // Message-Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isUserMessage) Color(0xFF353545) else Color(0xFF42424E),
            modifier = Modifier
                .align(if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart)
                .clickable(enabled = !isUserMessage) { showPopup = true }
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = Color.White
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