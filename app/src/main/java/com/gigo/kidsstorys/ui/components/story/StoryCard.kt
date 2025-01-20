package com.gigo.kidsstorys.ui.components.story

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.data.models.Story
import com.gigo.kidsstorys.ui.theme.AccentPurple
import androidx.compose.foundation.border

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernStoryCard(
    story: Story,
    onCardClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onLongClick: () -> Unit,
    titleColor: Color,
    previewColor: Color,
    previewSize: Int,
    titleSize: Int,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = onLongClick
            )
            .then(
                if (cardAlpha.value < 1.0f) {
                    Modifier.border(
                        width = 1.dp,
                        color = AccentPurple.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp)
                    )
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                AccentPurple.copy(alpha = cardAlpha.value)
            } else {
                Color(0xFF353545).copy(alpha = cardAlpha.value)
            }
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f * cardAlpha.value),
                            Color.Black.copy(alpha = 0.05f * cardAlpha.value)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = story.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = titleColor,
                        fontSize = titleSize.sp,
                        modifier = Modifier.weight(1f)
                    )

                    if (!isSelected) {
                        IconButton(
                            onClick = onOptionsClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_more),
                                contentDescription = "Optionen",
                                modifier = Modifier.size(24.dp),
                                tint = titleColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dynamische Box-Höhe basierend auf Bildverfügbarkeit
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (story.imagePath != null) {
                                Modifier.height(200.dp)
                            } else {
                                Modifier.height(100.dp)
                            }
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    if (story.imagePath != null) {
                        val bitmap = remember(story.imagePath) {
                            BitmapFactory.decodeFile(story.imagePath)
                        }
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Text(
                            text = story.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = previewColor,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = previewSize.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Ausgewählt",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}