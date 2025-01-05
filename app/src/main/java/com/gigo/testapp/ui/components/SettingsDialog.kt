package com.gigo.testapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gigo.testapp.ui.theme.*
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.gigo.testapp.data.SettingsManager

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    wrapText: Boolean,
    onWrapTextChanged: (Boolean) -> Unit,
    isDarkTheme: Boolean
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    
    var wrapText by remember { mutableStateOf(settingsManager.wrapText) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Einstellungen ⚙️",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isDarkTheme) TextLight else AccentPurple
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Zeilenumbruch-Option
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Zeilenumbruch",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isDarkTheme) TextLight else AccentPurple
                        )
                        Text(
                            "Text am Bildschirmrand umbrechen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDarkTheme) TextLight.copy(alpha = 0.7f) else AccentPurple.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = wrapText,
                        onCheckedChange = { newValue ->
                            wrapText = newValue
                            settingsManager.wrapText = newValue
                        }
                    )
                }
                
                HorizontalDivider()
                
                // Neue Option: Automatisches Speichern
                var autoSave by remember { mutableStateOf(true) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Automatisch speichern",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isDarkTheme) TextLight else AccentPurple
                        )
                        Text(
                            "Änderungen sofort speichern",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDarkTheme) TextLight.copy(alpha = 0.7f) else AccentPurple.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = autoSave,
                        onCheckedChange = { autoSave = it }
                    )
                }
                
                HorizontalDivider()
                
                // Neue Option: Schriftgröße für Vorschau
                var previewFontSize by remember { mutableStateOf(14f) }
                Column {
                    Text(
                        "Vorschau-Schriftgröße",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDarkTheme) TextLight else AccentPurple
                    )
                    Text(
                        "Schriftgröße in der Listenansicht",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) TextLight.copy(alpha = 0.7f) else AccentPurple.copy(alpha = 0.7f)
                    )
                    Slider(
                        value = previewFontSize,
                        onValueChange = { previewFontSize = it },
                        valueRange = 12f..20f,
                        steps = 7
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Schließen 🚪")
                }
            }
        }
    }
}

@Composable
fun HorizontalDivider() {
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
    )
} 