package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.AccentPurple

@Composable
fun StoryTopBar(
    storiesCount: Int,
    isCompactView: Boolean,
    onViewToggle: () -> Unit,
    onChatClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
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
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(24.dp)
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
                    "Geschichten",
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
                    "$storiesCount Geschichten",
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
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isCompactView) R.drawable.ic_grid_view
                            else R.drawable.ic_list_view
                        ),
                        contentDescription = if (isCompactView) "Zur Listenansicht wechseln" 
                        else "Zur Rasteransicht wechseln",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Chat Button
                FilledIconButton(
                    onClick = onChatClick,
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ai_chat),
                        contentDescription = "KI Chat öffnen",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Settings Button
                FilledIconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Einstellungen",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
} 