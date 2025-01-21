package com.gigo.kidsstorys.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.components.ConfettiConfig
import com.gigo.kidsstorys.ui.components.ConfettiHost
import com.gigo.kidsstorys.ui.components.ConfettiHostState
import com.gigo.kidsstorys.ui.theme.AccentPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Willkommen bei Kids Storys",
            description = "Entdecke eine magische Welt voller Geschichten! Hier kannst du kreative Kindergeschichten erschaffen, bearbeiten und sammeln. Lass deiner Fantasie freien Lauf! ✨",
            image = R.drawable.ic_books,
            backgroundColor = Color(0xFF1A1A2E)
        ),
        OnboardingPage(
            title = "Dein KI-Geschichtenhelfer",
            description = "Triff DeMa, deinen persönlichen KI-Assistenten! DeMa hilft dir dabei, einzigartige Geschichten zu entwickeln, gibt kreative Vorschläge und unterstützt dich beim Schreiben. Gemeinsam erschafft ihr zauberhafte Abenteuer! 🤖✨",
            image = R.drawable.ic_ai,
            backgroundColor = Color(0xFF1F1F35)
        ),
        OnboardingPage(
            title = "Personalisiere deine App",
            description = "Gestate deine Leseumgebung genau nach deinen Wünschen! Wähle Schriftgrößen, Farben und Layouts. Mit dem dunklen Design ist das Lesen besonders angenehm für die Augen. 🎨",
            image = R.drawable.ic_customize,
            backgroundColor = Color(0xFF252542)
        ),
        OnboardingPage(
            title = "Geschichten verwalten",
            description = "Organisiere deine Geschichten einfach und übersichtlich. Bearbeite und speichere Sie ganz einfach. Füge Bilder hinzu und mache deine Geschichten noch lebendiger! 📚",
            image = R.drawable.app_icon,
            backgroundColor = Color(0xFF2A2A4D)
        )
    )

    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { position ->
            OnboardingPageContent(
                page = pages[position],
                onNext = {
                    if (position < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(position + 1)
                        }
                    } else {
                        scope.launch {
                            onComplete()
                        }
                    }
                },
                isLastPage = position == pages.size - 1
            )
        }

        // Nur der Button und der Skip-Button bleiben
        Button(
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    scope.launch {
                        onComplete()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    spotColor = AccentPurple,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = AccentPurple.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentPurple
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (pagerState.currentPage == pages.size - 1) "Fertig" else "Weiter",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        TextButton(
            onClick = onComplete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    color = AccentPurple.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                "Überspringen",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    onNext: () -> Unit,
    isLastPage: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backgroundColor)
    ) {
        // Animierter Hintergrund für alle Seiten
        AnimatedBubblesBackground()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = page.image),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun AnimatedBubblesBackground() {
    val bubbles = remember {
        List(20) {
            Triple(
                Random.nextFloat(),  // x position
                Random.nextFloat(),  // y position
                Random.nextFloat() * 30 + 10  // size
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val movement by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        bubbles.forEach { (x, y, size) ->
            drawCircle(
                color = AccentPurple.copy(alpha = 0.2f),
                radius = size.dp.toPx(),
                center = Offset(
                    x * this.size.width + (movement - 0.5f) * 50f,
                    y * this.size.height + sin(movement * 2 * PI.toFloat()) * 30f
                )
            )
        }
    }
}

private data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
    val backgroundColor: Color
) 