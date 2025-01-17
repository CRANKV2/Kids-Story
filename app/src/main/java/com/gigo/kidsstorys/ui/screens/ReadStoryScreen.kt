@file:Suppress("SpellCheckingInspection")

package com.gigo.kidsstorys.ui.screens

import StoryImageEditDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.utils.ImageUtils
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    var showImagePicker by remember { mutableStateOf(false) }
    var isProcessingImage by remember { mutableStateOf(false) }
    var showImageEditDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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

    // Bildverarbeitungs-Logik (analog zu SettingsScreen)
    fun processSelectedImage(uri: Uri) {
        scope.launch {
            isProcessingImage = true
            story?.let { currentStory ->
                val imageFile = File(context.filesDir, "story_${currentStory.id}_image.jpg")
                val success = ImageUtils.processAndSaveImage(
                    context,
                    uri,
                    imageFile
                )
                if (success) {
                    viewModel.updateStoryImage(currentStory.id, imageFile.absolutePath)
                    Toast.makeText(
                        context,
                        "Klicke auf das Bild, um es zu ändern oder zu entfernen",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            isProcessingImage = false
            showImagePicker = false
        }
    }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processSelectedImage(it) }
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
                            .height(200.dp)
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        if (story?.imagePath != null) {
                            var offsetY by remember { mutableFloatStateOf(0f) }
                            var isDragging by remember { mutableStateOf(false) }
                            val context = LocalContext.current
                            
                            val bitmap = remember(story?.imagePath) {
                                BitmapFactory.decodeFile(story?.imagePath)
                            }
                            bitmap?.let {
                                val imageRatio = it.height.toFloat() / it.width.toFloat()
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scale(1.5f)
                                        .graphicsLayer { 
                                            translationY = offsetY 
                                        }
                                        .combinedClickable(
                                            onClick = { showImageEditDialog = true },
                                            onLongClick = {
                                                isDragging = true
                                                Toast.makeText(
                                                    context,
                                                    "Ziehe das Bild nach oben oder unten um den Ausschnitt anzupassen",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                        .pointerInput(Unit) {
                                            detectVerticalDragGestures(
                                                onDragEnd = {
                                                    if (isDragging) {
                                                        Toast.makeText(
                                                            context,
                                                            "Bildposition gespeichert",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        isDragging = false
                                                    }
                                                }
                                            ) { _, dragAmount ->
                                                if (isDragging) {
                                                    offsetY = (offsetY + dragAmount).coerceIn(-200f, 200f)
                                                }
                                            }
                                        },
                                    contentScale = ContentScale.FillWidth
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

                // Existierende Dialoge bleiben unverändert
                if (showEditDialog) {
                    Dialog(onDismissRequest = { showEditDialog = false }) {
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
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF2D2D3A)
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
                                            elevation = 8.dp,
                                            spotColor = AccentPurple,
                                            ambientColor = AccentPurple,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                                            elevation = 8.dp,
                                            spotColor = AccentPurple,
                                            ambientColor = AccentPurple,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                                    elevation = 16.dp,
                                    spotColor = AccentPurple,
                                    ambientColor = AccentPurple,
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF2D2D3A)
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
                                                elevation = 8.dp,
                                                spotColor = AccentPurple,
                                                ambientColor = AccentPurple,
                                                shape = RoundedCornerShape(20.dp)
                                            ),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                                    elevation = 16.dp,
                                    spotColor = AccentPurple,
                                    ambientColor = AccentPurple,
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF2D2D3A)
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
                                                elevation = 8.dp,
                                                spotColor = AccentPurple,
                                                ambientColor = AccentPurple,
                                                shape = RoundedCornerShape(20.dp)
                                            ),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
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

                // Dialog für die Bildbearbeitung
                if (showImageEditDialog) {
                    StoryImageEditDialog(
                        onDismiss = { showImageEditDialog = false },
                        onChangeImage = {
                            imagePickerLauncher.launch("image/*")
                            showImageEditDialog = false
                        },
                        onRemoveImage = {
                            story?.let { currentStory ->
                                viewModel.removeStoryImage(currentStory.id)
                            }
                            showImageEditDialog = false
                        }
                    )
                }
            }
        }
    }
}