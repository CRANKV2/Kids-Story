package com.gigo.kidsstorys.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.screens.about.components.*
import com.gigo.kidsstorys.ui.theme.TextLight

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AboutScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha by settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    val infiniteTransition = rememberInfiniteTransition(label = "border")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing)
        ),
        label = "borderRotation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                AboutTopBar(navController = navController, cardAlpha = cardAlpha)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    AboutAppCard(cardAlpha = cardAlpha, angle = angle)

                    AboutDeveloperCard(cardAlpha = cardAlpha, angle = angle)

                    Text(
                        "© 2024-2025 Kids Storys Inc.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextLight.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}