package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.R

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
                elevation = 4.dp,
                spotColor = MaterialTheme.colorScheme.primary,
                ambientColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp)
            ),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Geschichten",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "$storiesCount Geschichten",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View Toggle Button
                FilledIconButton(
                    onClick = onViewToggle,
                    modifier = Modifier.size(45.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isCompactView) R.drawable.ic_grid_view
                            else R.drawable.ic_list_view
                        ),
                        contentDescription = if (isCompactView) "Zur Listenansicht wechseln" 
                        else "Zur Rasteransicht wechseln",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                // Chat Button
                FilledIconButton(
                    onClick = onChatClick,
                    modifier = Modifier.size(45.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ai_chat),
                        contentDescription = "KI Chat öffnen",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                
                // Settings Button
                FilledIconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(45.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Einstellungen",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
} 