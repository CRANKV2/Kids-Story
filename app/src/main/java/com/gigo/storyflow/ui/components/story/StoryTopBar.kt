package com.gigo.storyflow.ui.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.storyflow.R
import com.gigo.storyflow.data.SettingsManager
import com.gigo.storyflow.ui.theme.AccentPurple

@Composable
fun StoryTopBar(
    storiesCount: Int,
    isCompactView: Boolean,
    onViewToggle: () -> Unit,
    onChatClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 16.dp * cardAlpha.value,
                spotColor = AccentPurple.copy(alpha = cardAlpha.value),
                ambientColor = AccentPurple.copy(alpha = cardAlpha.value),
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF353545).copy(alpha = cardAlpha.value),
                        Color(0xFF2D2D3A).copy(alpha = cardAlpha.value)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Linke Seite: Titel und Zähler
            Column {
                Text(
                    "Story Flow",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        shadow = Shadow(
                            color = AccentPurple.copy(alpha = 0.5f),
                            offset = Offset(0f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    color = Color.White
                )
                Text(
                    if (storiesCount == 1) "1 Geschichte" else "$storiesCount Geschichten",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Rechte Seite: Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View Toggle Button
                FilledIconButton(
                    onClick = onViewToggle,
                    modifier = Modifier.size(45.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isCompactView) R.drawable.grid_1
                            else R.drawable.grid_2
                        ),
                        contentDescription = if (isCompactView) "Zur Listenansicht wechseln" 
                        else "Zur Rasteransicht wechseln",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(27.dp)
                    )
                }
                
                // Chat Button
                FilledIconButton(
                    onClick = onChatClick,
                    modifier = Modifier.size(45.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ai),
                        contentDescription = "KI Chat öffnen",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(27.dp)
                    )
                }
                
                // Settings Button
                FilledIconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(45.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings_icon),
                        contentDescription = "Einstellungen",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(27.dp)
                    )
                }
            }
        }
    }
} 