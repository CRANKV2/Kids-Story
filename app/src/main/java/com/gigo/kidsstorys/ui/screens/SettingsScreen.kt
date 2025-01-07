package com.gigo.kidsstorys.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.theme.*
import com.gigo.kidsstorys.ui.components.HorizontalDivider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.components.ColorPicker
import com.gigo.kidsstorys.ui.viewmodels.StoryViewModel
import androidx.compose.ui.geometry.Offset

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
                        stringResource(R.string.einstellungen),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Text(stringResource(R.string.tipps_left_emoji), fontSize = 24.sp)
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
                stringResource(R.string.hauptbildschirm_h1),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
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
                        stringResource(R.string.hauptbildschirm),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF9575CD)
                    )

                    // Titel-Schriftgröße
                    Column {
                        Text(
                            stringResource(R.string.titel_schriftgroesse),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextLight
                        )
                        Text(
                            stringResource(R.string.schriftgroesse_der_titel_in_der_bersicht),
                            style = MaterialTheme.typography.bodyMedium,
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
                                .heightIn(min = 16.dp),
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
                            style = MaterialTheme.typography.bodyMedium,
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
                                .heightIn(min = 16.dp),
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
                    containerColor = Color(0xFF2D2D3A)
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
                    containerColor = Color(0xFF2D2D3A)
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
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
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
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
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
            SettingsFooter()
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

@Composable
fun SettingsFooter() {
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
            containerColor = Color(0xFF2D2D3A)
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