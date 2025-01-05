package com.gigo.kidsstorys.ui.screens

import android.app.Activity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageRes: Int,
    val text: String,
    val backgroundColor: Color
)

val onboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.onboarding1,
        text = "Willkommen bei Kindermärchen!\nHier warten magische Abenteuer auf dich! 🌟",
        backgroundColor = Color(0xFF9C27B0)  // Violett
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding2,
        text = "Entdecke spannende Geschichten\nund träume mit uns! 🌈",
        backgroundColor = Color(0xFF2196F3)  // Blau
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding3,
        text = "Lass uns zusammen auf eine\nmagische Reise gehen! ✨",
        backgroundColor = Color(0xFF4CAF50)  // Grün
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(onboardingPages[pagerState.currentPage].backgroundColor)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Bild mit Animation und Schatten
                Image(
                    painter = painterResource(id = onboardingPages[page].imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(24.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Text mit Animation
                Text(
                    text = onboardingPages[page].text,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        // Bubbles
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            onboardingPages.forEachIndexed { index, _ ->
                val size by animateDpAsState(
                    targetValue = if (pagerState.currentPage == index) 16.dp else 10.dp,
                    label = "bubble size"
                )
                
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(size)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Kinderfreundlicher "Beenden" Button
            Button(
                onClick = { activity.finish() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B)  // Sanftes Rot
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.shadow(8.dp, RoundedCornerShape(20.dp))
            ) {
                Text(
                    "Tschüss! 👋",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Kinderfreundlicher "Weiter/Starten" Button
            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinished()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)  // Freundliches Grün
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.shadow(8.dp, RoundedCornerShape(20.dp))
            ) {
                Text(
                    if (pagerState.currentPage < onboardingPages.size - 1) 
                        "Weiter! 🚀" else "Los geht's! 🎉",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 