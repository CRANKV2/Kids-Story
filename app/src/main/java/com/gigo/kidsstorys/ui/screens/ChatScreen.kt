package com.gigo.kidsstorys.ui.screens

import LoadingDots
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
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
                        ChatMessageItem(message)
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
fun ChatMessageItem(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .shadow(
                    elevation = 8.dp,
                    spotColor = if (message.isUser) AccentPurple else Color(0xFF353545),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                ),
            color = if (message.isUser) AccentPurple else Color(0xFF353545),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            )
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 