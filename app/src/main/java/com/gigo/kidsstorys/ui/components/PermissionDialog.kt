package com.gigo.kidsstorys.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Speicher-Berechtigung") },
        text = { 
            Text("Diese App benötigt Zugriff auf den Speicher, um Geschichten und Medien zu speichern und zu laden.")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Berechtigung erteilen")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Später")
            }
        }
    )
} 