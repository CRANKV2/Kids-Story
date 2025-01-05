package com.gigo.kidsstorys.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.*

@Composable
fun AddStoryDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var story by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.neue_geschichte),
                style = MaterialTheme.typography.titleLarge,
                color = TextLight
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.titel), color = TextLight) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TextLight,
                        unfocusedBorderColor = TextLight.copy(alpha = 0.5f),
                        focusedLabelColor = TextLight,
                        unfocusedLabelColor = TextLight.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = story,
                    onValueChange = { story = it },
                    label = { Text(stringResource(R.string.geschichte), color = TextLight) },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TextLight,
                        unfocusedBorderColor = TextLight.copy(alpha = 0.5f),
                        focusedLabelColor = TextLight,
                        unfocusedLabelColor = TextLight.copy(alpha = 0.5f)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, story) },
                enabled = title.isNotBlank() && story.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPurple
                )
            ) {
                Text(stringResource(R.string.speichern))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.abbrechen), color = TextLight)
            }
        }
    )
} 