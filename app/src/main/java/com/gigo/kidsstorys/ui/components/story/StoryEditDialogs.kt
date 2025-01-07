package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.models.Story
import com.gigo.kidsstorys.ui.theme.*

@Composable
fun StoryEditOptionsDialog(
    onDismiss: () -> Unit,
    onEditContent: () -> Unit,
    onEditTitle: () -> Unit,
    isDarkTheme: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                stringResource(R.string.was_moechtest_du_bearbeiten),
                color = TextLight
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = {
                        onEditTitle()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.titel_bearbeiten))
                }
                Button(
                    onClick = {
                        onEditContent()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.inhalt_bearbeiten))
                }
            }
        },
        containerColor = if (isDarkTheme) CardDark else CardLight
    )
}

@Composable
fun StoryContentEditDialog(
    story: Story,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    isDarkTheme: Boolean
) {
    var editedContent by remember { mutableStateOf(story.content) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(R.string.inhalt_bearbeiten),
                color = TextLight
            )
        },
        text = {
            TextField(
                value = editedContent,
                onValueChange = { editedContent = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = if (isDarkTheme) Color(0xFF353545) else Color(0xFFE0E0E0),
                    focusedContainerColor = if (isDarkTheme) Color(0xFF353545) else Color(0xFFE0E0E0)
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(editedContent)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.speichern), color = TextLight)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.abbrechen), color = TextLight)
            }
        },
        containerColor = if (isDarkTheme) CardDark else CardLight
    )
}

@Composable
fun StoryTitleEditDialog(
    story: Story,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    isDarkTheme: Boolean
) {
    var editedTitle by remember { mutableStateOf(story.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(R.string.titel_bearbeiten),
                color = TextLight
            )
        },
        text = {
            TextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = if (isDarkTheme) Color(0xFF353545) else Color(0xFFE0E0E0),
                    focusedContainerColor = if (isDarkTheme) Color(0xFF353545) else Color(0xFFE0E0E0)
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(editedTitle)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.speichern), color = TextLight)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.abbrechen), color = TextLight)
            }
        },
        containerColor = if (isDarkTheme) CardDark else CardLight
    )
} 