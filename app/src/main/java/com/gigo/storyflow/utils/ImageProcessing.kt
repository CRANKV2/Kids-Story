package com.gigo.storyflow.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.gigo.storyflow.data.models.Story
import com.gigo.storyflow.ui.viewmodels.StoryViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun rememberImagePicker(
    context: Context,
    story: Story?,
    viewModel: StoryViewModel,
    onImageProcessed: (String) -> Unit
): () -> Unit {
    val scope = rememberCoroutineScope()
    var isProcessingImage by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                isProcessingImage = true
                try {
                    story?.let { currentStory ->
                        val imageFile = File(context.filesDir, "story_${currentStory.id}_image.jpg")
                        val success = ImageUtils.processAndSaveImage(
                            context,
                            uri,
                            imageFile
                        )
                        if (success) {
                            viewModel.updateStoryImage(currentStory.id, imageFile.absolutePath)
                            onImageProcessed(imageFile.absolutePath)
                            Toast.makeText(
                                context,
                                "Klicke auf das Bild, um es zu ändern oder zu entfernen",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } finally {
                    isProcessingImage = false
                }
            }
        }
    }

    return { imagePickerLauncher.launch("image/*") }
}