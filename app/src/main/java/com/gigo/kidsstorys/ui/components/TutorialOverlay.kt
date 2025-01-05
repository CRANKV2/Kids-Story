package com.gigo.kidsstorys.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight

@Composable
fun TutorialOverlay(
    onDismiss: () -> Unit
) {
    var currentTip by remember { mutableStateOf(0) }
    
    val tips = listOf(
        "Halte eine Geschichte lange gedrückt, um sie zu bearbeiten oder zu löschen 📝",
        "Nutze die Einstellungen um verschiedene App-Elemente anzupassen und zu verwalten ⚙️",
        "Tippe auf eine Geschichte um sie zu lesen, oder erstelle eine neue mit dem ➕ Button"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D3A)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Tipp ${currentTip + 1}/3",
                        style = MaterialTheme.typography.titleMedium,
                        color = AccentPurple
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        tips[currentTip],
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextLight,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (currentTip > 0) {
                    Button(
                        onClick = { currentTip-- },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF353545)
                        )
                    ) {
                        Text(stringResource(R.string.zurueck), color = TextLight)
                    }
                }
                
                if (currentTip < 2) {
                    Button(
                        onClick = { currentTip++ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF353545)
                        )
                    ) {
                        Text(stringResource(R.string.weiter), color = TextLight)
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentPurple
                        )
                    ) {
                        Text(stringResource(R.string.fertig))
                    }
                }
            }
        }
    }
} 