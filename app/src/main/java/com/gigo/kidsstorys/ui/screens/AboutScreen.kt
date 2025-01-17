package com.gigo.kidsstorys.ui.screens

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gigo.kidsstorys.R
import com.gigo.kidsstorys.data.SettingsManager
import com.gigo.kidsstorys.ui.theme.AccentPurple
import com.gigo.kidsstorys.ui.theme.TextLight
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager.getInstance(context) }
    val cardAlpha by settingsManager.cardAlpha.collectAsState(initial = 0.75f)

    // Animate Bubbles
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
        // Animate Hintergrund
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
            // Animated Bubbles
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

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    color = AccentPurple.copy(alpha = cardAlpha),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    shadowElevation = 8.dp
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "Über die App",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (46f / LocalDensity.current.density).sp
                                ),
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Zurück",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
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

                    // App Logo mit Animation und Schatten
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = CircleShape,
                                spotColor = AccentPurple
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_icon),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    // App Info Card mit animiertem Border
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xFF2D2D3A).copy(alpha = cardAlpha),
                        border = BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AccentPurple,
                                    Color(0xFF00BCD4),  // Cyan
                                    Color(0xFF9C27B0),  // Purple
                                    Color(0xFF00BCD4),  // Cyan
                                    AccentPurple
                                ),
                                start = Offset(
                                    x = cos(angle * PI.toFloat() / 180) * 100f,
                                    y = sin(angle * PI.toFloat() / 180) * 100f
                                ),
                                end = Offset(
                                    x = cos((angle + 180f) * PI.toFloat() / 180) * 100f,
                                    y = sin((angle + 180f) * PI.toFloat() / 180) * 100f
                                )
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Kids Storys",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextLight
                            )

                            val packageInfo = remember {
                                try {
                                    context.packageManager.getPackageInfo(context.packageName, 0)
                                } catch (e: PackageManager.NameNotFoundException) {
                                    null
                                }
                            }

                            Text(
                                "Version ${packageInfo?.versionName ?: "unknown"}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextLight.copy(alpha = 0.7f)
                            )

                            Text(
                                "Release: 10. Januar 2025",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextLight.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Developer Card mit animiertem Border
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = AccentPurple.copy(alpha = cardAlpha),
                        border = BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE91E63),  // Pink
                                    Color(0xFF2196F3),  // Blue
                                    Color(0xFFE91E63)   // Pink
                                ),
                                start = Offset(
                                    x = cos((angle + 90f) * PI.toFloat() / 180) * 100f,
                                    y = sin((angle + 90f) * PI.toFloat() / 180) * 100f
                                ),
                                end = Offset(
                                    x = cos((angle + 270f) * PI.toFloat() / 180) * 100f,
                                    y = sin((angle + 270f) * PI.toFloat() / 180) * 100f
                                )
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Entwickelt mit ❤️ von",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                "Francesco De Martino",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Copyright mit Animation
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