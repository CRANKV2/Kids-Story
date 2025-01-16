package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.data.models.Story

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernStoryCard(
    story: Story,
    onCardClick: () -> Unit,
    onOptionsClick: () -> Unit,
    titleColor: Color,
    previewColor: Color,
    previewSize: Int,
    titleSize: Int,
    modifier: Modifier = Modifier
) {
    // SettingsManager für die Transparenz
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha = settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = onOptionsClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF353545).copy(alpha = cardAlpha.value)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        // Füge einen transparenten Hintergrund hinzu für bessere Lesbarkeit
        Box(
            modifier = Modifier
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
                modifier = Modifier.padding(16.dp)
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

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onOptionsClick,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more),
                            contentDescription = "Optionen",
                            modifier = Modifier.size(24.dp),
                            tint = titleColor // Passt die Icon-Farbe an die Titel-Farbe an
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = story.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = previewColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = previewSize.sp
                )
            }
        }
    }
}