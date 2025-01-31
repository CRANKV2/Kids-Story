package com.gigo.storyflow.ui.screens.about.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gigo.storyflow.ui.theme.AccentPurple
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AboutDeveloperCard(
    cardAlpha: Float,
    angle: Float
) {
    val cardColor = AccentPurple.copy(alpha = cardAlpha)
    val gradientStartOffset = calculateGradientOffset(angle + 90f)
    val gradientEndOffset = calculateGradientOffset(angle + 270f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = cardColor,
        border = BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFE91E63),
                    Color(0xFF2196F3),
                    Color(0xFFE91E63)
                ),
                start = gradientStartOffset,
                end = gradientEndOffset
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Entwickelt mit ❤️ von",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Francesco De Martino",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun calculateGradientOffset(angle: Float): Offset {
    val radians = angle * PI.toFloat() / 180
    return Offset(cos(radians) * 100f, sin(radians) * 100f)
}