package com.gigo.kidsstorys.ui.components

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
import com.gigo.kidsstorys.data.Story
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight

@Composable
fun ModernStoryCard(
    story: Story,
    onCardClick: () -> Unit,
    onOptionsClick: () -> Unit,
    titleColor: Color,
    previewColor: Color,
    previewSize: Int,
    isCompactView: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onCardClick)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = AccentPurple.copy(alpha = 0.5f),
                ambientColor = AccentPurple.copy(alpha = 0.3f)
            ),
        color = Color(0xFF2D2D3A),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(if (isCompactView) 20.dp else 16.dp)
                .wrapContentHeight(),
            horizontalAlignment = if (!isCompactView) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                text = story.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (!isCompactView) 20.sp else 22.sp
                ),
                color = titleColor,
                textAlign = if (!isCompactView) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            if (!isCompactView) {
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = onOptionsClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF353545), CircleShape)
                ) {
                    Text("⋮", fontSize = 24.sp, color = TextLight)
                }
            }
        }
    }
} 