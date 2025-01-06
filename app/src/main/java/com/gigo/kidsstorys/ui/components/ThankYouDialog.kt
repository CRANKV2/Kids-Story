package com.gigo.kidsstorys.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.ui.theme.AccentPurple

@Composable
fun ThankYouDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "🎉 Juhu! Los geht's!",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = { 
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Danke, dass du mir vertraust! Lass uns zusammen magische Geschichten erschaffen! ✨",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Viel Spaß beim Entdecken! 🚀",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPurple
                )
            ) {
                Text("Auf geht's! 🎨")
            }
        },
        containerColor = Color(0xFF2D2D3A)
    )
} 