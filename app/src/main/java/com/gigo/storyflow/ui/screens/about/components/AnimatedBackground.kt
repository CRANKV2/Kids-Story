package com.gigo.storyflow.ui.screens.about.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gigo.storyflow.ui.theme.AccentPurple

@Composable
fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "bubbles")
    val bubbleOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = BUBBLE_ANIMATION_DURATION_1, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bubble1"
    )
    val bubbleOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = BUBBLE_ANIMATION_DURATION_2, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bubble2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AccentPurple.copy(alpha = 0.8f),
                        BACKGROUND_COLOR
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBubble(
                offsetMultiplierX = 0.2f,
                offsetMultiplierY = 0.3f,
                offsetMultiplier = 0.1f,
                bubbleOffsetX = bubbleOffset1,
                bubbleOffsetY = bubbleOffset2,
                radius = BUBBLE_RADIUS_1,
                color = Color.White.copy(alpha = 0.1f)
            )
            drawBubble(
                offsetMultiplierX = 0.8f,
                offsetMultiplierY = 0.7f,
                offsetMultiplier = -0.1f,
                bubbleOffsetX = bubbleOffset2,
                bubbleOffsetY = bubbleOffset1,
                radius = BUBBLE_RADIUS_2,
                color = Color.White.copy(alpha = 0.05f)
            )
        }
    }
}

private fun Canvas.drawBubble(
    offsetMultiplierX: Float,
    offsetMultiplierY: Float,
    offsetMultiplier: Float,
    bubbleOffsetX: Float,
    bubbleOffsetY: Float,
    radius: Dp,
    color: Color
) {
    drawCircle(
        color = color,
        radius = radius.toPx(),
        center = Offset(
            x = size.width * offsetMultiplierX + (size.width * offsetMultiplier * bubbleOffsetX),
            y = size.height * offsetMultiplierY + (size.height * offsetMultiplier * bubbleOffsetY)
        )
    )
}

// Constants
private const val BUBBLE_ANIMATION_DURATION_1 = 5000
private const val BUBBLE_ANIMATION_DURATION_2 = 7000
private val BACKGROUND_COLOR = Color(0xFF1A1A1A)
private val BUBBLE_RADIUS_1 = 200.dp
private val BUBBLE_RADIUS_2 = 300.dp