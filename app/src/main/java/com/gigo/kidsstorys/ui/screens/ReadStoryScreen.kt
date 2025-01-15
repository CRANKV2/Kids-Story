package com.gigo.kidsstorys.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.gigo.kidsstorys.R
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.content.ContentResolver
import android.net.Uri
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

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
    val fontSize = remember { mutableStateOf(settingsManager.fontSize.toFloat()) }
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

    // Neuer Scale-State für Zoom
    var scale by remember { mutableStateOf(1f) }
    val scaleState = rememberTransformableState { zoomChange, _, _ ->
        scale = (scale * zoomChange).coerceIn(0.5f..3f)
    }

    val backgroundImageFile = remember {
        File(context.filesDir, "background_image.jpg")
    }
    var backgroundBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val backgroundAlpha = remember { settingsManager.backgroundAlpha }

    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    LaunchedEffect(storyId) {
        viewModel.loadStory(storyId)
    }

    LaunchedEffect(userPreferences.fontSize) {
        fontSize.value = userPreferences.fontSize.toFloat()
    }

    LaunchedEffect(Unit) {
        if (backgroundImageFile.exists()) {
            backgroundBitmap = BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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

        story?.let { currentStory ->
            var currentTitle by remember { mutableStateOf(currentStory.title) }
            var currentContent by remember { mutableStateOf(currentStory.content) }
            
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text(currentStory.title, color = Color.White) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Zurück",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { showEditDialog = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_edit),
                                    contentDescription = "Bearbeiten",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = cardAlpha.value),
                            navigationIconContentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .shadow(
                                elevation = 8.dp * cardAlpha.value,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                spotColor = AccentPurple.copy(alpha = cardAlpha.value)
                            )
                            .clip(RoundedCornerShape(24.dp))
                    )
                },
                containerColor = Color.Transparent
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
                            .padding(16.dp)
                            .shadow(
                                elevation = 16.dp * cardAlpha.value,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                ambientColor = AccentPurple.copy(alpha = cardAlpha.value)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .background(Color.Transparent)
                        ) {
                            Text(
                                text = currentTitle,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = titleColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AssistChip(
                                onClick = { },
                                label = { Text(stringResource(R.string.read_story_screen_geschichte)) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF353545).copy(alpha = cardAlpha.value),
                                    labelColor = AccentPurple.copy(alpha = maxOf(cardAlpha.value + 0.2f, 1f))
                                )
                            )
                        }
                    }

                    // Geschichte-Sektion
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .shadow(
                                elevation = 16.dp * cardAlpha.value,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                ambientColor = AccentPurple.copy(alpha = cardAlpha.value)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(Color.Transparent)
                        ) {
                            if (wrapText) {
                                Text(
                                    text = currentStory.content,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = fontSize.value.sp
                                    ),
                                    color = textColor,
                                    modifier = Modifier
                                        .verticalScroll(verticalScrollState)
                                        .padding(8.dp)
                                )
                            } else {
                                Text(
                                    text = currentStory.content,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = fontSize.value.sp
                                    ),
                                    color = textColor,
                                    modifier = Modifier
                                        .horizontalScroll(horizontalScrollState)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }

                // Existierende Dialoge bleiben unverändert
                if (showEditDialog) {
                    Dialog(onDismissRequest = { showEditDialog = false }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .shadow(
                                    elevation = 16.dp * cardAlpha.value,
                                    spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = cardAlpha.value)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Was möchtest du bearbeiten?",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        shadow = Shadow(
                                            color = AccentPurple.copy(alpha = 0.5f),
                                            offset = Offset(0f, 2f),
                                            blurRadius = 4f
                                        )
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                FilledTonalButton(
                                    onClick = { 
                                        showEditDialog = false
                                        showContentEditDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .shadow(
                                            elevation = 8.dp * cardAlpha.value,
                                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = cardAlpha.value)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        stringResource(R.string.geschichte_bearbeiten),
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                FilledTonalButton(
                                    onClick = { 
                                        showEditDialog = false
                                        showTitleEditDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .shadow(
                                            elevation = 8.dp * cardAlpha.value,
                                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = cardAlpha.value)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        stringResource(R.string.titel_bearbeiten),
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                // Dialog für die Bearbeitung des Inhalts
                if (showContentEditDialog) {
                    Dialog(onDismissRequest = { showContentEditDialog = false }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .wrapContentHeight()
                                .shadow(
                                    elevation = 16.dp * cardAlpha.value,
                                    spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = cardAlpha.value)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                                    .imePadding(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    stringResource(R.string.geschichte_bearbeiten),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        shadow = Shadow(
                                            color = AccentPurple.copy(alpha = 0.5f),
                                            offset = Offset(0f, 2f),
                                            blurRadius = 4f
                                        )
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                BasicTextField(
                                    value = currentContent,
                                    onValueChange = { currentContent = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 150.dp, max = 300.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = cardAlpha.value),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    textStyle = TextStyle(
                                        color = Color.White,
                                        fontSize = 16.sp
                                    ),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            if (currentContent.isEmpty()) {
                                                Text(
                                                    stringResource(R.string.geschichte),
                                                    color = Color.White.copy(alpha = 0.5f)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        onClick = { showContentEditDialog = false },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            stringResource(R.string.abbrechen),
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    FilledTonalButton(
                                        onClick = {
                                            viewModel.updateStory(currentStory.copy(content = currentContent))
                                            showContentEditDialog = false
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(
                                                elevation = 8.dp * cardAlpha.value,
                                                spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                                ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                                shape = RoundedCornerShape(20.dp)
                                            ),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = cardAlpha.value)
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    ) {
                                        Text(
                                            stringResource(R.string.speichern),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Dialog für die Bearbeitung des Titels
                if (showTitleEditDialog) {
                    Dialog(onDismissRequest = { showTitleEditDialog = false }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .wrapContentHeight()
                                .shadow(
                                    elevation = 16.dp * cardAlpha.value,
                                    spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = cardAlpha.value)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                                    .imePadding(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    stringResource(R.string.titel_bearbeiten),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        shadow = Shadow(
                                            color = AccentPurple.copy(alpha = 0.5f),
                                            offset = Offset(0f, 2f),
                                            blurRadius = 4f
                                        )
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                BasicTextField(
                                    value = currentTitle,
                                    onValueChange = { currentTitle = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = cardAlpha.value),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    textStyle = TextStyle(
                                        color = Color.White,
                                        fontSize = 16.sp
                                    ),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            if (currentTitle.isEmpty()) {
                                                Text(
                                                    stringResource(R.string.titel),
                                                    color = Color.White.copy(alpha = 0.5f)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        onClick = { showTitleEditDialog = false },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            stringResource(R.string.abbrechen),
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    FilledTonalButton(
                                        onClick = {
                                            viewModel.updateStory(currentStory.copy(title = currentTitle))
                                            showTitleEditDialog = false
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(
                                                elevation = 8.dp * cardAlpha.value,
                                                spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                                                ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                                                shape = RoundedCornerShape(20.dp)
                                            ),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = cardAlpha.value)
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    ) {
                                        Text(
                                            stringResource(R.string.speichern),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
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
} 