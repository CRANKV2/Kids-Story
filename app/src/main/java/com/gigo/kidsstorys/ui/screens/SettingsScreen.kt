package com.gigo.kidsstorys.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.theme.*
import com.gigo.kidsstorys.ui.components.HorizontalDivider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.ui.components.ColorPicker
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Einstellungen",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Text("⬅️", fontSize = 24.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
        containerColor = if (isDarkTheme) Color(0xFF1E1E2E) else Color(0xFFF5F5F5)
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
                "Hauptbildschirm",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            // Hauptbildschirm Einstellungen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D3A)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "📱 Hauptbildschirm",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF9575CD)
                    )

                    // Titel-Schriftgröße
                    Column {
                        Text(
                            "Titel-Schriftgröße",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextLight
                        )
                        Text(
                            "Schriftgröße der Titel in der Übersicht",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextLight.copy(alpha = 0.7f)
                        )
                        Slider(
                            value = titleSize,
                            onValueChange = { 
                                titleSize = it
                                settingsManager.updateTitleSize(it.toInt())
                            },
                            valueRange = 16f..24f,
                            steps = 7
                        )
                        Text(
                            "Beispiel-Titel 📚",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = titleSize.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextLight
                        )
                    }

                    HorizontalDivider(color = Color(0xFF353545))

                    // Vorschau-Schriftgröße
                    Column {
                        Text(
                            "Vorschau-Schriftgröße",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextLight
                        )
                        Text(
                            "Schriftgröße der Geschichten-Vorschau",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextLight.copy(alpha = 0.7f)
                        )
                        Slider(
                            value = previewSize,
                            onValueChange = { 
                                previewSize = it
                                settingsManager.updatePreviewSize(it.toInt())
                            },
                            valueRange = 12f..20f,
                            steps = 7
                        )
                        Text(
                            "Beispiel Text ✨",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = previewSize.sp
                            ),
                            color = TextLight,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Leseansicht Sektion
            Text(
                "Leseansicht",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Leseansicht Einstellungen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D3A)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "🎨 Leseansicht",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF9575CD)
                    )
                    
                    // Schriftgröße für Leseansicht
                    Column {
                        Text(
                            "Schriftgröße beim Lesen",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextLight
                        )
                        Text(
                            "Betrifft nur die Schriftgröße beim Lesen einer Geschichte",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextLight.copy(alpha = 0.7f)
                        )
                        Slider(
                            value = fontSize,
                            onValueChange = {
                                fontSize = it
                                settingsManager.fontSize = it.toInt()
                            },
                            valueRange = 12f..24f,
                            steps = 11
                        )
                        Text(
                            "Beispiel-Text ✨",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = fontSize.sp
                            ),
                            color = TextLight
                        )
                    }

                    HorizontalDivider(color = Color(0xFF353545))

                    // Zeilenumbruch
                    Column {
                        Text(
                            "Zeilenumbruch (empfohlen)",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextLight
                        )
                        Text(
                            "Automatischer Zeilenumbruch beim Lesen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextLight.copy(alpha = 0.7f)
                        )
                        Switch(
                            checked = wrapText,
                            onCheckedChange = {
                                wrapText = it
                                settingsManager.wrapText = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF9575CD),
                                checkedTrackColor = Color(0xFF353545)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Farbeinstellungen Sektion
            Text(
                "Farbeinstellungen",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Neue Karte für Farbeinstellungen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D3A)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "🎨 Farbeinstellungen",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF9575CD)
                    )
                    
                    // Hauptbildschirm Farben
                    Text(
                        "Hauptbildschirm",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextLight
                    )
                    
                    // Vorschau der Karte
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF353545)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Beispiel Titel",
                                style = MaterialTheme.typography.titleLarge,
                                color = cardTitleColor
                            )
                            Text(
                                "Beispiel Vorschautext der Geschichte...",
                                color = cardPreviewColor
                            )
                        }
                    }
                    
                    // Farbauswahl für Kartentitel
                    ColorPicker(
                        title = "Titel-Farbe",
                        currentColor = cardTitleColor,
                        onColorSelected = { newColor: Color ->
                            cardTitleColor = newColor
                            viewModel.updateCardTitleColor(newColor)
                        }
                    )
                    
                    // Farbauswahl für Kartenvorschau
                    ColorPicker(
                        title = "Vorschau-Farbe",
                        currentColor = cardPreviewColor,
                        onColorSelected = { newColor: Color ->
                            cardPreviewColor = newColor
                            viewModel.updateCardPreviewColor(newColor)
                        }
                    )
                    
                    Divider(color = Color(0xFF353545))
                    
                    // Leseansicht Farben
                    Text(
                        "Leseansicht",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextLight
                    )
                    
                    // Vorschau der Leseansicht
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF353545)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Beispiel Titel",
                                style = MaterialTheme.typography.titleLarge,
                                color = storyTitleColor
                            )
                            Text(
                                "Beispiel Geschichtentext...",
                                color = storyTextColor
                            )
                        }
                    }
                    
                    // Farbauswahl für Leseansicht Titel
                    ColorPicker(
                        title = "Geschichte Titel-Farbe",
                        currentColor = storyTitleColor,
                        onColorSelected = { newColor: Color ->
                            storyTitleColor = newColor
                            viewModel.updateStoryTitleColor(newColor)
                        }
                    )
                    
                    // Farbauswahl für Leseansicht Text
                    ColorPicker(
                        title = "Geschichte Text-Farbe",
                        currentColor = storyTextColor,
                        onColorSelected = { newColor: Color ->
                            storyTextColor = newColor
                            viewModel.updateStoryTextColor(newColor)
                        }
                    )
                }
            }
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
                text = "✔️",
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