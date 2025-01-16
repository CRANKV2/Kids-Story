package com.gigo.kidsstorys.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.gigo.kidsstorys.data.SettingsManager

private val DarkColorScheme = darkColorScheme(
    primary = AccentPurple,
    secondary = AccentPurpleLight,
    background = BackgroundDark,
    surface = CardDark,
    onPrimary = TextLight,
    onSecondary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun KidsStorysTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun getCardBackgroundColor(hasBackground: Boolean): Color {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)
    
    return if (hasBackground) {
        Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
    } else {
        Color(0xFF2D2D3A)
    }
}