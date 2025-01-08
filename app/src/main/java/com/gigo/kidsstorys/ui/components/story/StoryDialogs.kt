package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.*

@Composable
fun StoryTutorialDialog(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean
) {
    var currentTipIndex by remember { mutableStateOf(0) }
    val tips = listOf(
        "Tippe auf + um eine neue Geschichte zu erstellen",
        "Tippe auf eine Geschichte um sie zu lesen",
        "Tippe auf ⚙️ um die Einstellungen zu öffnen",
        "Tippe auf ⋮ um eine Geschichte zu bearbeiten oder zu löschen",
        "In den Einstellungen kannst du Textgrößen und Farben anpassen"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color(0xFF9575CD)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF2D2D3A) else Color(0xFFF5F5F5)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.tipps_tricks),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )

                Text(
                    text = tips[currentTipIndex],
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = TextLight,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (currentTipIndex > 0) currentTipIndex-- },
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = if (isDarkTheme) Color(0xFF353545) else Color(0xFFEEEEEE),
                                shape = CircleShape
                            )
                    ) {
                        Text(
                            stringResource(R.string.tipps_tricks_back),
                            fontSize = 32.sp
                        )
                    }
                    
                    Text(
                        "${currentTipIndex + 1}/${tips.size}",
                        fontSize = 18.sp,
                        color = TextLight
                    )
                    
                    IconButton(
                        onClick = { if (currentTipIndex < tips.size - 1) currentTipIndex++ },
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = if (isDarkTheme) Color(0xFF353545) else Color(0xFFEEEEEE),
                                shape = CircleShape
                            )
                    ) {
                        Text(
                            stringResource(R.string.tipps_tricks_forward),
                            fontSize = 32.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = MaterialTheme.colorScheme.primary
                        )
                ) {
                    Text(
                        stringResource(R.string.verstanden),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StoryDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
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
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.geschichte_loeschen),
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

                Text(
                    stringResource(R.string.frage_wirklich_loeschen),
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(R.string.abbrechen),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFFBA1A1A).copy(alpha = 0.12f)
                        )
                    ) {
                        Text(
                            stringResource(R.string.loeschen),
                            color = Color(0xFFBA1A1A),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
} 