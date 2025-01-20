package com.gigo.kidsstorys.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.components.dialogs.ContentEditDialog
import com.gigo.kidsstorys.ui.components.dialogs.EditDialog
import com.gigo.kidsstorys.ui.components.dialogs.ImageEditDialog
import com.gigo.kidsstorys.ui.components.dialogs.TitleEditDialog
import com.gigo.kidsstorys.ui.components.story.StoryContent
import com.gigo.kidsstorys.ui.components.story.StoryImage
import com.gigo.kidsstorys.ui.components.story.StoryTitle
import com.gigo.kidsstorys.ui.components.topbar.StoryTopBar
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.utils.rememberImagePicker
import java.io.File

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
    ) { _ ->
        // Bildverarbeitung Callback
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                StoryTopBar(
                    onBack = onBack,
                    onEdit = { showEditDialog = true },
                    cardAlpha = cardAlpha.value
                )

                StoryImage(
                    imagePath = story?.imagePath,
                    onImageClick = { showImageEditDialog = true },
                    onAddImage = { imagePickerLauncher() }
                )

                StoryTitle(
                    title = currentTitle,
                    titleColor = titleColor,
                    cardAlpha = cardAlpha.value,
                    onDoubleClick = { showTitleEditDialog = true }
                )

                StoryContent(
                    content = currentStory.content,
                    textColor = textColor,
                    fontSize = fontSize.floatValue,
                    wrapText = wrapText,
                    cardAlpha = cardAlpha.value,
                    onDoubleClick = { showContentEditDialog = true },
                    modifier = Modifier.weight(1f)
                )
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

    if (showImageEditDialog) {
        ImageEditDialog(
            onDismiss = { showImageEditDialog = false },
            onChangeImage = {
                imagePickerLauncher()
                showImageEditDialog = false
            },
            onRemoveImage = {
                story?.let { currentStory ->
                    viewModel.updateStoryImage(currentStory.id, null)
                }
                showImageEditDialog = false
            }
        )
    }
}