package com.gigo.kidsstorys.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.Story
import com.gigo.kidsstorys.ui.theme.*

@Composable
fun StoryOptionsDialog(
    story: Story,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isDarkTheme: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = if (isDarkTheme) CardDark else CardLight
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "${story.title}",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextLight
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Bearbeiten Button
                Button(
                    onClick = {
                        onEdit()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple
                    )
                ) {
                    Text(stringResource(R.string.bearbeiten), fontSize = 18.sp)
                }

                // Löschen Button
                Button(
                    onClick = {
                        onDelete()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple
                    )
                ) {
                    Text(stringResource(R.string.l_schen), fontSize = 18.sp)
                }

                // Schließen Button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        stringResource(R.string.schliessen),
                        fontSize = 18.sp,
                        color = TextLight
                    )
                }
            }
        }
    }
} 