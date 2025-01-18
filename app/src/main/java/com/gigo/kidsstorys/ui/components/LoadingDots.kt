package com.gigo.kidsstorys.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object LoadingDots {
    @SuppressLint("UseOfNonLambdaOffsetOverload")
    @Composable
    operator fun invoke() {
        val dots = listOf(".", ".", ".")
        val infiniteTransition = rememberInfiniteTransition(label = "loading")
        
        val dotAnimations = List(dots.size) { index ->
            infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, delayMillis = index * 100),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot$index"
            )
        }

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            dotAnimations.forEach { animation ->
                Text(
                    text = ".",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.offset(y = animation.value.dp)
                )
            }
        }
    }
}