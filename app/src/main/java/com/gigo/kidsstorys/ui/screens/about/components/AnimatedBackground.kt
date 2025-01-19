package com.gigo.kidsstorys.ui.screens.about.components

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
import androidx.compose.ui.unit.dp
import com.gigo.kidsstorys.ui.theme.AccentPurple

@Composable
fun AnimatedBackground() {
    val infiniteTransitionbubbles = rememberInfiniteTransition(label = "bubbles")
    val bubbleOffset1 by infiniteTransitionbubbles.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bubble1"
    )
    val bubbleOffset2 by infiniteTransitionbubbles.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bubble2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AccentPurple.copy(alpha = 0.8f),
                        Color(0xFF1A1A1A)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 200.dp.toPx(),
                center = Offset(
                    x = size.width * 0.2f + (size.width * 0.1f * bubbleOffset1),
                    y = size.height * 0.3f + (size.height * 0.1f * bubbleOffset2)
                )
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = 300.dp.toPx(),
                center = Offset(
                    x = size.width * 0.8f - (size.width * 0.1f * bubbleOffset2),
                    y = size.height * 0.7f - (size.height * 0.1f * bubbleOffset1)
                )
            )
        }
    }
} 