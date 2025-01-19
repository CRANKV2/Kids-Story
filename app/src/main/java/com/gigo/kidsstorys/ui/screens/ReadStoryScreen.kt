package com.gigo.kidsstorys.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.components.dialogs.ContentEditDialog
import com.gigo.kidsstorys.ui.components.dialogs.EditDialog
import com.gigo.kidsstorys.ui.components.dialogs.TitleEditDialog
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.utils.rememberImagePicker
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadStoryScreen(
    storyId: Int,
    onBack: () -> Unit,
    viewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory)
) {
    val story by viewModel.selectedStory.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showContentEditDialog by remember { mutableStateOf(false) }
    var showTitleEditDialog by remember { mutableStateOf(false) }
    var showImageEditDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val fontSize = remember { mutableFloatStateOf(settingsManager.fontSize.toFloat()) }
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

    val backgroundImageFile = remember {
        File(context.filesDir, "background_image.jpg")
    }
    var backgroundBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val backgroundAlpha = remember { settingsManager.backgroundAlpha }

    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    val imagePickerLauncher = rememberImagePicker(
        context = context,
        story = story,
        viewModel = viewModel
    ) { imagePath ->
        // Hier können Sie direkt den Code ausführen, der nach der Bildverarbeitung laufen soll
    }

    var currentTitle by remember(story) { mutableStateOf(story?.title ?: "") }
    var currentContent by remember(story) { mutableStateOf(story?.content ?: "") }

    LaunchedEffect(storyId) {
        viewModel.loadStory(storyId)
    }

    LaunchedEffect(userPreferences.fontSize) {
        fontSize.floatValue = userPreferences.fontSize
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
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text("Geschichte", color = Color.White) },
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
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Bild oder Button-Bereich
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (story?.imagePath != null) 200.dp else 56.dp)
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        if (story?.imagePath != null) {
                            // Bild anzeigen mit standard Image composable
                            val bitmap = remember(story?.imagePath) {
                                BitmapFactory.decodeFile(story?.imagePath)
                            }
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { showImageEditDialog = true },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        } else {
                            // Button anzeigen
                            FilledTonalButton(
                                onClick = { imagePickerLauncher() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    "Bild zur Geschichte hinzufügen",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Titel-Sektion
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
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
                                color = titleColor,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Geschichte-Sektion
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 4.dp)
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
                                        fontSize = fontSize.floatValue.sp
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
                                        fontSize = fontSize.floatValue.sp
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
            }
        }
    }

    // Dialoge
    if (showEditDialog) {
        EditDialog(
            onDismiss = { showEditDialog = false },
            onContentEdit = { showContentEditDialog = true },
            onTitleEdit = { showTitleEditDialog = true }
        )
    }

    if (showContentEditDialog) {
        story?.let { currentStory ->
            ContentEditDialog(
                currentContent = currentContent,
                onDismiss = { showContentEditDialog = false },
                onSave = { newContent ->
                    viewModel.updateStory(currentStory.copy(content = newContent))
                    currentContent = newContent
                    showContentEditDialog = false
                }
            )
        }
    }

    if (showTitleEditDialog) {
        story?.let { currentStory ->
            TitleEditDialog(
                currentTitle = currentTitle,
                onDismiss = { showTitleEditDialog = false },
                onSave = { newTitle ->
                    viewModel.updateStory(currentStory.copy(title = newTitle))
                    currentTitle = newTitle
                    showTitleEditDialog = false
                }
            )
        }
    }
}