package com.gigo.kidsstorys.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.components.ColorPicker
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import com.gigo.kidsstorys.utils.ImageUtils
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.io.File



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    viewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory)
) {
    val userPreferences by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Initialisiere die State-Variablen mit den gespeicherten Werten
    var fontSize by remember { mutableStateOf(settingsManager.fontSize.toFloat()) }
    var wrapText by remember { mutableStateOf(settingsManager.wrapText) }
    var titleSize by remember { mutableStateOf(settingsManager.titleSize.toFloat()) }
    var previewSize by remember { mutableStateOf(settingsManager.previewSize.toFloat()) }

    // Farb-States werden aus den Preferences initialisiert
    var cardTitleColor by remember(userPreferences.cardTitleColor) {
        mutableStateOf(Color(userPreferences.cardTitleColor.toInt()))
    }
    var cardPreviewColor by remember(userPreferences.cardPreviewColor) {
        mutableStateOf(Color(userPreferences.cardPreviewColor.toInt()))
    }
    var storyTitleColor by remember(userPreferences.storyTitleColor) {
        mutableStateOf(Color(userPreferences.storyTitleColor.toInt()))
    }
    var storyTextColor by remember(userPreferences.storyTextColor) {
        mutableStateOf(Color(userPreferences.storyTextColor.toInt()))
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var showImagePickerDialog by remember { mutableStateOf(false) }
    var isProcessingImage by remember { mutableStateOf(false) }

    // Bildverarbeitungs-Logik
    fun processSelectedImage(uri: Uri) {
        scope.launch {
            isProcessingImage = true
            val success = ImageUtils.processAndSaveImage(context, uri)
            isProcessingImage = false
            showImagePickerDialog = false

            val message = if (success) {
                "Hintergrundbild wurde erfolgreich gespeichert"
            } else {
                "Fehler beim Speichern des Hintergrundbildes"
            }

            snackbarHostState.showSnackbar(message)
        }
    }

    // Aktualisieren Sie den imagePickerLauncher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            processSelectedImage(it)
        }
    }

    // Vorschau des aktuellen Hintergrundbildes
    val backgroundImageFile = remember {
        File(context.filesDir, "background_image.jpg")
    }

    // Nach der backgroundImageFile Definition (ca. Zeile 164)
    Box(modifier = Modifier.fillMaxSize()) {
        // Hintergrundbild
        if (backgroundImageFile.exists()) {
            val bitmap = remember {
                BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
            }
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.einstellungen),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Zurück",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AccentPurple,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = AccentPurple
                        )
                        .clip(RoundedCornerShape(24.dp))
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            },

            ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Hauptbildschirm Sektion
                Text(
                    stringResource(R.string.hauptbildschirm_h1),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                // Hauptbildschirm Einstellungen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (backgroundImageFile.exists()) {
                            Color(0xFF2D2D3A).copy(alpha = 0.75f)
                        } else {
                            Color(0xFF2D2D3A)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            stringResource(R.string.hauptbildschirm),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF9575CD)
                        )

                        // Titel-Schriftgröße
                        Column {
                            Text(
                                stringResource(R.string.titel_schriftgroesse),
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextLight
                            )
                            Text(
                                stringResource(R.string.schriftgroesse_der_titel_in_der_bersicht),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight.copy(alpha = 0.7f)
                            )
                            Slider(
                                value = titleSize,
                                onValueChange = {
                                    titleSize = it
                                    settingsManager.updateTitleSize(it.toInt())
                                },
                                valueRange = 12f..32f,
                                steps = 19,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .heightIn(min = 12.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = AccentPurple,
                                    activeTrackColor = AccentPurple,
                                    inactiveTrackColor = AccentPurple.copy(alpha = 0.3f)
                                )
                            )
                            Text(
                                stringResource(R.string.beispiel_text),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = titleSize.sp
                                ),
                                color = TextLight,
                                modifier = Modifier
                                    .fillMaxWidth() // Occupy the full width of the Column
                                    .wrapContentSize(Alignment.Center) // Center content within itself
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        colors = listOf(
                                            AccentPurple.copy(alpha = 0.0f),
                                            AccentPurple.copy(alpha = 0.7f),
                                            AccentPurple.copy(alpha = 0.0f)
                                        )
                                    )
                                )
                        )

                        // Vorschau-Schriftgröße
                        Column {
                            Text(
                                stringResource(R.string.vorschau_schriftgroesse),
                                style = MaterialTheme.typography.titleMedium,
                                color = TextLight
                            )
                            Text(
                                stringResource(R.string.schriftgroee_der_geschichten_vorschau),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight.copy(alpha = 0.7f)
                            )
                            Slider(
                                value = previewSize,
                                onValueChange = {
                                    previewSize = it
                                    settingsManager.updatePreviewSize(it.toInt())
                                },
                                valueRange = 12f..32f,
                                steps = 19,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .heightIn(min = 12.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = AccentPurple,
                                    activeTrackColor = AccentPurple,
                                    inactiveTrackColor = AccentPurple.copy(alpha = 0.3f)
                                )
                            )
                            Text(
                                stringResource(R.string.beispiel_text),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = previewSize.sp
                                ),
                                color = TextLight,
                                modifier = Modifier
                                    .fillMaxWidth() // Occupy the full width of the Column
                                    .wrapContentSize(Alignment.Center) // Center content within itself
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Hintergrundbild-Einstellung
                        Column {
                            Text(
                                "Hintergrundbild",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextLight
                            )
                            Text(
                                "Wähle ein Hintergrundbild für den Hauptbildschirm",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 6.dp,
                                        spotColor = AccentPurple,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF353545)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "Hintergrundbild auswählen",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextLight,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            // Image Picker Dialog
                            if (showImagePickerDialog) {
                                Dialog(onDismissRequest = { showImagePickerDialog = false }) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .shadow(
                                                elevation = 16.dp,
                                                spotColor = AccentPurple,
                                                shape = RoundedCornerShape(24.dp)
                                            ),
                                        shape = RoundedCornerShape(24.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            if (isProcessingImage) {
                                                CircularProgressIndicator(color = AccentPurple)
                                                Text(
                                                    "Bild wird verarbeitet...",
                                                    color = TextLight,
                                                    modifier = Modifier.padding(top = 16.dp)
                                                )
                                            } else {
                                                Text(
                                                    "Hintergrundbild auswählen",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = TextLight
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Button(
                                                    onClick = { imagePickerLauncher.launch("image/*") },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = AccentPurple
                                                    )
                                                ) {
                                                    Text("Aus Galerie wählen")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Vorschau des aktuellen Hintergrundbildes
                        if (backgroundImageFile.exists()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Vorschau
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Image(
                                    bitmap = remember {
                                        BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
                                            .asImageBitmap()
                                    },
                                    contentDescription = "Aktuelles Hintergrundbild",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Reset-Button
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        backgroundImageFile.delete()
                                        snackbarHostState.showSnackbar("Hintergrundbild wurde entfernt")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Hintergrundbild entfernen",
                                    color = Color.Red.copy(alpha = 0.7f),
                                    modifier = Modifier.wrapContentWidth(Alignment.End)
                                )
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))

                // Leseansicht Titel
                Text(
                    stringResource(R.string.leseansicht),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                // Leseansicht Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (backgroundImageFile.exists()) {
                            Color(0xFF2D2D3A).copy(alpha = 0.75f)
                        } else {
                            Color(0xFF2D2D3A)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.leseansicht),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF9575CD)
                        )

                        // Nur noch Zeilenumbruch-Option
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    stringResource(R.string.zeilenumbruch_empfohlen),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextLight
                                )
                                Text(
                                    stringResource(R.string.automatischer_zeilenumbruch_beim_lesen),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextLight.copy(alpha = 0.7f)
                                )
                            }
                            Switch(
                                checked = wrapText,
                                onCheckedChange = {
                                    wrapText = it
                                    settingsManager.wrapText = it
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AccentPurple,
                                    checkedTrackColor = AccentPurple.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Farbeinstellungen Sektion
                Text(
                    stringResource(R.string.farbeinstellungen),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )


                // Neue Karte für Farbeinstellungen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (backgroundImageFile.exists()) {
                            Color(0xFF2D2D3A).copy(alpha = 0.75f)
                        } else {
                            Color(0xFF2D2D3A)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.farbeinstellungen_h2),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF9575CD)
                        )

                        // Hauptbildschirm Farben
                        Text(
                            stringResource(R.string.hauptbildschirm),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF9575CD),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Vorschau der Karte
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (backgroundImageFile.exists()) {
                                    Color(0xFF2D2D3A).copy(alpha = 0.75f)
                                } else {
                                    Color(0xFF2D2D3A)
                                }
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    stringResource(R.string.beispiel_titel),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = cardTitleColor
                                )
                                Text(
                                    stringResource(R.string.beispiel_vorschautext_der_geschichte),
                                    color = cardPreviewColor
                                )
                            }
                        }

                        // Farbauswahl für Kartentitel
                        ColorPicker(
                            title = stringResource(R.string.titel_farbe),
                            currentColor = cardTitleColor,
                            onColorSelected = { newColor: Color ->
                                cardTitleColor = newColor
                                viewModel.updateCardTitleColor(newColor)
                            }
                        )

                        // Farbauswahl für Kartenvorschau
                        ColorPicker(
                            title = stringResource(R.string.vorschau_farbe),
                            currentColor = cardPreviewColor,
                            onColorSelected = { newColor: Color ->
                                cardPreviewColor = newColor
                                viewModel.updateCardPreviewColor(newColor)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        colors = listOf(
                                            AccentPurple.copy(alpha = 0.0f),
                                            AccentPurple.copy(alpha = 0.7f),
                                            AccentPurple.copy(alpha = 0.0f)
                                        )
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Leseansicht Farben
                        Text(
                            stringResource(R.string.leseansicht),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF9575CD),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Vorschau der Leseansicht
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (backgroundImageFile.exists()) {
                                    Color(0xFF2D2D3A).copy(alpha = 0.75f)
                                } else {
                                    Color(0xFF2D2D3A)
                                }
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    stringResource(R.string.beispiel_titel),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = storyTitleColor
                                )
                                Text(
                                    stringResource(R.string.beispiel_geschichtentext),
                                    color = storyTextColor
                                )
                            }
                        }

                        // Farbauswahl für Leseansicht Titel
                        ColorPicker(
                            title = stringResource(R.string.geschichte_titel_farbe),
                            currentColor = storyTitleColor,
                            onColorSelected = { newColor: Color ->
                                storyTitleColor = newColor
                                viewModel.updateStoryTitleColor(newColor)
                            }
                        )

                        // Farbauswahl für Leseansicht Text
                        ColorPicker(
                            title = stringResource(R.string.geschichte_text_farbe),
                            currentColor = storyTextColor,
                            onColorSelected = { newColor: Color ->
                                storyTextColor = newColor
                                viewModel.updateStoryTextColor(newColor)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                SettingsFooter(hasBackground = backgroundImageFile.exists())
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    @Composable
    fun ColorButton(
        color: Color,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (color.luminance() > 0.5f) Color.Black else Color.White,
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text(
                    text = stringResource(R.string.color_picker_checked),
                    fontSize = 20.sp,
                    color = if (color.luminance() > 0.5f) Color.Black else Color.White
                )
            }
        }
    }

    // Verwendung in der ColorPicker-Komponente:
    @Composable
    fun ColorPicker(
        colors: List<Color>,
        selectedColor: Color,
        onColorSelected: (Color) -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colors.forEach { color ->
                ColorButton(
                    color = color,
                    isSelected = selectedColor == color,
                    onClick = { onColorSelected(color) }
                )
            }
        }
    }
}

@Composable
fun SettingsFooter(hasBackground: Boolean = false) {  // Parameter hinzugefügt
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 8.dp,
                spotColor = AccentPurple,
                shape = RoundedCornerShape(24.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (hasBackground) {
                Color(0xFF2D2D3A).copy(alpha = 0.75f)
            } else {
                Color(0xFF2D2D3A)
            }
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Oberer Gradient-Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                AccentPurple.copy(alpha = 0.0f),
                                AccentPurple.copy(alpha = 0.7f),
                                AccentPurple.copy(alpha = 0.0f)
                            )
                        )
                    )
            )

            Text(
                "© 2025-2026",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    shadow = Shadow(
                        color = AccentPurple.copy(alpha = 0.5f),
                        offset = Offset(0f, 2f),
                        blurRadius = 4f
                    )
                ),
                color = AccentPurple
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Zentrierte Spalte für "Made with ❤️" und Namen
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Made with",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            color = AccentPurple.copy(alpha = 0.5f),
                            offset = Offset(0f, 2f),
                            blurRadius = 3f
                        )
                    ),
                    color = TextLight.copy(alpha = 0.9f)
                )

                Text(
                    "❤️",
                    fontSize = 28.sp,
                    modifier = Modifier.shadow(
                        elevation = 8.dp,
                        spotColor = AccentPurple
                    )
                )

                Text(
                    "Francesco De Martino",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        shadow = Shadow(
                            color = AccentPurple.copy(alpha = 0.6f),
                            offset = Offset(0f, 2f),
                            blurRadius = 5f
                        )
                    ),
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    AccentPurple,
                                    AccentPurple.copy(alpha = 0.7f)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.White
                )
            }

            // Unterer Gradient-Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                AccentPurple.copy(alpha = 0.0f),
                                AccentPurple.copy(alpha = 0.7f),
                                AccentPurple.copy(alpha = 0.0f)
                            )
                        )
                    )
            )
        }
    }
}

