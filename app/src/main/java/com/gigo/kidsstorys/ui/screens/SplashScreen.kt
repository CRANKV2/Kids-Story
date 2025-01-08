package com.gigo.kidsstorys.ui.screens

import android.content.Context
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
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        ),
        label = "Alpha Animation"
    )

    val context = LocalContext.current
    val sharedPrefs = remember {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    val isFirstLaunch = remember {
        sharedPrefs.getBoolean("is_first_launch", true)
    }

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1500)
        
        if (isFirstLaunch) {
            sharedPrefs.edit().putBoolean("is_first_launch", false).apply()
            onFirstLaunch()
        } else {
            onTimeout()
        }
    }

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
                modifier = Modifier.alpha(alphaAnim.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.geschichten_app),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) TextLight else AccentPurple,
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
} 