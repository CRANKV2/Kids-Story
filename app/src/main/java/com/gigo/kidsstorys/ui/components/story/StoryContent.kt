package com.gigo.kidsstorys.ui.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.ui.theme.AccentPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StoryContent(
    content: String,
    textColor: Color,
    fontSize: Float,
    wrapText: Boolean,
    cardAlpha: Float,
    onDoubleClick: () -> Unit = {}
) {
    var lastTapTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(
                elevation = 16.dp * cardAlpha,
                shape = RoundedCornerShape(24.dp),
                spotColor = AccentPurple.copy(alpha = cardAlpha),
                ambientColor = AccentPurple.copy(alpha = cardAlpha)
            )
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .then(
                    if (wrapText) {
                        Modifier.verticalScroll(rememberScrollState())
                    } else {
                        Modifier.horizontalScroll(rememberScrollState())
                    }
                )
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = fontSize.sp
                ),
                color = textColor
            )
        }
    }
} 