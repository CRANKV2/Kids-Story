@file:Suppress("SpellCheckingInspection", "NAME_SHADOWING")

package com.gigo.kidsstorys.ui.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.components.ColorPicker
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import com.gigo.kidsstorys.utils.ImageUtils
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: StoryViewModel = viewModel(factory = StoryViewModel.Factory)
) {
    val userPreferences by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // ScrollVerhalten für die TopBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Initialisiere die State-Variablen mit den gespeicherten Werten
    var wrapText by remember { mutableStateOf(settingsManager.wrapText) }
    var titleSize by remember { mutableFloatStateOf(settingsManager.titleSize.toFloat()) }
    var previewSize by remember { mutableFloatStateOf(settingsManager.previewSize.toFloat()) }
    var backgroundAlpha by remember { mutableFloatStateOf(settingsManager.backgroundAlpha) }

    // Farb-States werden aus den Preferences initialised
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

    // Füge diese Zeile hinzu
    val coroutineScope = rememberCoroutineScope()

    var showImagePickerDialog by remember { mutableStateOf(false) }
    var isProcessingImage by remember { mutableStateOf(false) }

    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    val backgroundImageFile = File(context.filesDir, "background_image.jpg")
    var backgroundBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    // Initialisiere das Bitmap, wenn die Datei existiert
    LaunchedEffect(Unit) {
        if (backgroundImageFile.exists()) {
            backgroundBitmap = BitmapFactory.decodeFile(backgroundImageFile.absolutePath)
        }
    }

    // Bildverarbeitungs-Logik
    fun processSelectedImage(uri: Uri) {
        scope.launch {
            isProcessingImage = true
            val backgroundFile = File(context.filesDir, "background_image.jpg")
            val success = ImageUtils.processAndSaveImage(
                context, 
                uri,
                backgroundFile  // Für Hintergrundbild immer diese Datei verwenden
            )
            if (success) {
                backgroundBitmap = BitmapFactory.decodeFile(backgroundFile.absolutePath)
            }
            isProcessingImage = false
            showImagePickerDialog = false
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

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            stringResource(R.string.einstellungen),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = (45f / LocalDensity.current.density).sp
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
                        containerColor = AccentPurple.copy(alpha = cardAlpha.value),
                        titleContentColor = Color.White,
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
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                // Gemeinsame Darstellungseinstellungen Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp * cardAlpha.value,
                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Darstellung",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF9575CD)
                        )

                        // Titel-Schriftgröße (vorher in Hauptbildschirm)
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

                        // Trennlinie
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        AccentPurple.copy(alpha = 0.0f),
                                        AccentPurple.copy(alpha = 0.7f),
                                        AccentPurple.copy(alpha = 0.0f)
                                    )
                                )
                            )
                        )

                        // Vorschau-Schriftgröße (vorher in Hauptbildschirm)
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

                        // Trennlinie
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        AccentPurple.copy(alpha = 0.0f),
                                        AccentPurple.copy(alpha = 0.7f),
                                        AccentPurple.copy(alpha = 0.0f)
                                    )
                                )
                            )
                        )

                        // Hintergrundbild-Einstellungen
                        Column {
                            Text(
                                "Hintergrundbild",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextLight
                            )
                            
                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                                )
                            ) {
                                Text(
                                    "Hintergrundbild auswählen",
                                    color = TextLight
                                )
                            }

                            // Vorschau des aktuellen Hintergrundbildes
                            if (backgroundBitmap != null) {
                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                ) {
                                    Image(
                                        bitmap = backgroundBitmap!!.asImageBitmap(),
                                        contentDescription = "Aktuelles Hintergrundbild",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        alpha = backgroundAlpha
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Hintergrund-Transparenz Einstellungen
                                Text(
                                    "Hintergrund-Transparenz",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextLight
                                )
                                
                                Slider(
                                    value = backgroundAlpha,
                                    onValueChange = { 
                                        backgroundAlpha = it
                                        settingsManager.backgroundAlpha = it
                                    },
                                    valueRange = 0.1f..1f,
                                    steps = 9,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = SliderDefaults.colors(
                                        thumbColor = AccentPurple,
                                        activeTrackColor = AccentPurple,
                                        inactiveTrackColor = AccentPurple.copy(alpha = 0.3f)
                                    )
                                )

                                Text(
                                    "${(backgroundAlpha * 100).toInt()}% Sichtbarkeit",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextLight,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            backgroundImageFile.delete()
                                            backgroundBitmap = null
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

                        // Trennlinie
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        AccentPurple.copy(alpha = 0.0f),
                                        AccentPurple.copy(alpha = 0.7f),
                                        AccentPurple.copy(alpha = 0.0f)
                                    )
                                )
                            )
                        )

                        // Karten-Transparenz (war bereits in Darstellung)
                        Column {
                            Text(
                                "Karten-Transparenz",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextLight
                            )
                            Text(
                                "Stelle die Transparenz aller Karten in der App ein (0% = unsichtbar, 100% = voll sichtbar)",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight.copy(alpha = 0.7f)
                            )

                            val cardAlpha by settingsManager.cardAlpha.collectAsState(initial = 0.75f)

                            Slider(
                                value = cardAlpha,
                                onValueChange = { newAlpha ->
                                    coroutineScope.launch {
                                        settingsManager.updateCardAlpha(newAlpha)
                                    }
                                },
                                valueRange = 0f..1f,
                                steps = 9, // 10 Schritte (0, 0.1, 0.2, ..., 0.9, 1.0)
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = AccentPurple,
                                    activeTrackColor = AccentPurple,
                                    inactiveTrackColor = AccentPurple.copy(alpha = 0.3f)
                                )
                            )

                            Text(
                                "Aktuelle Transparenz: ${(cardAlpha * 100).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextLight,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Trennlinie
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        AccentPurple.copy(alpha = 0.0f),
                                        AccentPurple.copy(alpha = 0.7f),
                                        AccentPurple.copy(alpha = 0.0f)
                                    )
                                )
                            )
                        )

                        // Zeilenumbruch-Option (war bereits in Darstellung)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.padding(end = 16.dp)  // Abstand zwischen Text und Switch
                            ) {
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

                // Neue Karte für Farbeinstellungen
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp * cardAlpha.value,
                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
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
                                containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
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
                                    brush = Brush.horizontalGradient(
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
                                containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
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

                // Problembehandlung-Sektion
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(
                            elevation = 8.dp * cardAlpha.value,
                            spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                            ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Problembehandlung",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = TextLight,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                        
                        Text(
                            "Diese Funktionen können bei Problemen mit der App helfen. Das Löschen des Cache entfernt nur temporäre Dateien. Deine Geschichten, Bilder, der Hintergrund und alle Einstellungen bleiben dabei erhalten.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = TextLight.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    try {
                                        // Interner Cache
                                        context.cacheDir.deleteRecursively()
                                        // Externer Cache
                                        context.externalCacheDir?.deleteRecursively()
                                        // Temporäre Dateien
                                        context.filesDir.listFiles()?.forEach { file ->
                                            if (!file.name.endsWith(".jpg") && !file.name.endsWith(".db")) {
                                                file.delete()
                                            }
                                        }
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Cache wurde gelöscht")
                                        }
                                    } catch (e: Exception) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Fehler beim Löschen des Cache")
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentPurple
                                )
                            ) {
                                Text(
                                    "Cache löschen",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Button(
                                onClick = {
                                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                    Runtime.getRuntime().exit(0)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentPurple
                                )
                            ) {
                                Text(
                                    "App neu starten",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Über die App Button
                Button(
                    onClick = { navController.navigate("about") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                    )
                ) {
                    Text(
                        "Über die App",
                        color = TextLight,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // App Version
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                    ) {
                        Text(
                            "Build: Stardust b52cd",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = TextLight.copy(alpha = 0.8f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

