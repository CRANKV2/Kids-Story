package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.ui.theme.AccentPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StoryTitle(
    title: String,
    titleColor: Color,
    cardAlpha: Float,
    onDoubleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var lastTapTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 8.dp * cardAlpha,
                spotColor = AccentPurple.copy(alpha = cardAlpha),
                ambientColor = AccentPurple.copy(alpha = cardAlpha),
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                if (cardAlpha < 1.0f) {
                    Modifier.border(
                        width = 1.dp,
                        color = AccentPurple.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else Modifier
            ),
        color = Color(0xFF2D2D3A).copy(alpha = cardAlpha),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(vertical = 4.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastTapTime < 300) {
                                    coroutineScope.launch {
                                        onDoubleClick()
                                        delay(300)
                                    }
                                }
                                lastTapTime = currentTime
                            }
                        )
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D3A).copy(alpha = cardAlpha)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = titleColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
} 