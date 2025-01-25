package com.gigo.storyflow.ui.components.story

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.storyflow.ui.theme.AccentPurple

@Composable
fun StoryContent(
    content: String,
    textColor: Color,
    fontSize: Float,
    wrapText: Boolean,
    cardAlpha: Float,
    onDoubleClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var lastTapTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
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
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = fontSize.sp
            ),
            color = textColor,
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
        )
    }
}