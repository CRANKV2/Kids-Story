package com.gigo.kidsstorys.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ConfettiHostState {
    private var isPlaying by mutableStateOf(false)
    
    suspend fun showConfetti() {
        isPlaying = true
    }
    
    internal fun isConfettiPlaying(): Boolean = isPlaying
    internal fun stopConfetti() {
        isPlaying = false
    }
}

data class ConfettiConfig(
    val confettiSize: Dp = 12.dp,
    val fadeOut: Boolean = true,
    val timeToLive: Int = 2000
)

@Composable
fun ConfettiHost(
    hostState: ConfettiHostState,
    modifier: Modifier = Modifier,
    confettiConfig: ConfettiConfig = ConfettiConfig()
) {
    val scope = rememberCoroutineScope()
    val particles = remember { mutableStateListOf<ConfettiParticle>() }
    
    LaunchedEffect(hostState.isConfettiPlaying()) {
        if (hostState.isConfettiPlaying()) {
            repeat(100) {
                particles.add(
                    ConfettiParticle(
                        color = getRandomConfettiColor(),
                        size = confettiConfig.confettiSize
                    )
                )
            }
            
            scope.launch {
                kotlinx.coroutines.delay(confettiConfig.timeToLive.toLong())
                hostState.stopConfetti()
                particles.clear()
            }
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            rotate(particle.rotation) {
                drawPath(
                    path = particle.path,
                    color = particle.color.copy(
                        alpha = if (confettiConfig.fadeOut) {
                            particle.alpha
                        } else 1f
                    )
                )
            }
            
            particle.update(size)
        }
    }
}

private class ConfettiParticle(
    val color: Color,
    size: Dp
) {
    private val pixelSize = size.value
    var x by mutableStateOf(0f)
    var y by mutableStateOf(0f)
    var rotation by mutableStateOf(0f)
    var alpha by mutableStateOf(1f)
    private val gravity = 9.81f
    private var velocity = Offset(
        Random.nextFloat() * 400 - 200,
        Random.nextFloat() * 200 + 100
    )
    
    val path = Path().apply {
        moveTo(-pixelSize/2, -pixelSize/2)
        lineTo(pixelSize/2, -pixelSize/2)
        lineTo(pixelSize/2, pixelSize/2)
        lineTo(-pixelSize/2, pixelSize/2)
        close()
    }
    
    init {
        x = Random.nextFloat() * 1000
        y = -pixelSize
    }
    
    fun update(canvasSize: androidx.compose.ui.geometry.Size) {
        if (y < canvasSize.height) {
            x += velocity.x
            y += velocity.y
            velocity = velocity.copy(y = velocity.y + gravity * 0.016f)
            rotation += Random.nextFloat() * 20 - 10
            alpha = (1 - y / canvasSize.height).coerceIn(0f, 1f)
        }
        
        if (x < 0 || x > canvasSize.width) {
            velocity = velocity.copy(x = -velocity.x * 0.8f)
        }
    }
}

private fun getRandomConfettiColor(): Color {
    return listOf(
        Color(0xFFE57373), // Rot
        Color(0xFF81C784), // Grün
        Color(0xFF64B5F6), // Blau
        Color(0xFFFFB74D), // Orange
        Color(0xFFBA68C8), // Lila
        Color(0xFF4FC3F7), // Hellblau
        Color(0xFFFFF176)  // Gelb
    ).random()
} 