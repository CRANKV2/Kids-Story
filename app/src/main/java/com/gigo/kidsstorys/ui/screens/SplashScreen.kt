// SplashScreen.kt
package com.gigo.kidsstorys.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight
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
        animationSpec = tween(durationMillis = 1000),
        label = "Alpha Animation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1500)
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
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.books),
                fontSize = 72.sp,
                modifier = Modifier.alpha(alpha)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.geschichten_app),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) TextLight else AccentPurple,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}
