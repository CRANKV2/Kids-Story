package com.gigo.kidsstorys.ui.components.story

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StoryImage(
    imagePath: String?,
    onImageClick: () -> Unit,
    onAddImage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (imagePath != null) 200.dp else 56.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        if (imagePath != null) {
            val bitmap = remember(imagePath) {
                BitmapFactory.decodeFile(imagePath)
            }
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onImageClick() },
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            FilledTonalButton(
                onClick = onAddImage,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    "Bild zur Geschichte hinzufügen",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp
                )
            }
        }
    }
} 