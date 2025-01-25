// SplashScreen.kt
package com.gigo.storyflow.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gigo.storyflow.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isDarkTheme: Boolean,
    onTimeout: () -> Unit,
    onFirstLaunch: () -> Unit
) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    val isFirstLaunch = remember { sharedPrefs.getBoolean("is_first_launch", true) }

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "Alpha Animation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1000)
        handleLaunch(isFirstLaunch, sharedPrefs, onFirstLaunch, onTimeout)
    }

    SplashContent(isDarkTheme, alphaAnim.value)
}

private fun handleLaunch(
    isFirstLaunch: Boolean,
    sharedPrefs: SharedPreferences,
    onFirstLaunch: () -> Unit,
    onTimeout: () -> Unit
) {
    if (isFirstLaunch) {
        sharedPrefs.edit().putBoolean("is_first_launch", false).apply()
        onFirstLaunch()
    } else {
        onTimeout()
    }
}

@Composable
private fun SplashContent(isDarkTheme: Boolean, alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha)
        )
    }
}
