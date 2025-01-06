package com.gigo.kidsstorys.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gigo.kidsstorys.R

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