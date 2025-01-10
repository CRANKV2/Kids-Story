package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.data.models.Story
import com.gigo.kidsstorys.ui.theme.TextLight
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.res.painterResource
import com.gigo.kidsstorys.R

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
    isCompactView: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = onOptionsClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF353545)
        ),
        shape = RoundedCornerShape(16.dp)
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
                    modifier = Modifier.weight(1f) // Gibt dem Text eine flexible Breite, behält aber Platz für das Icon
                )

                Spacer(modifier = Modifier.width(8.dp)) // Fügt einen kleinen horizontalen Abstand zwischen Text und Icon hinzu

                IconButton(
                    onClick = onOptionsClick,
                    modifier = Modifier
                        .size(36.dp) // Reduzierte Größe für das Icon
                        .padding(0.dp) // Entfernt jegliches Padding innerhalb des IconButton
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "Optionen",
                        modifier = Modifier.size(24.dp) // Stellt die tatsächliche Größe des Icons ein
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