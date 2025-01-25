package com.gigo.storyflow.ui.screens.about.components

import android.content.pm.PackageManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gigo.storyflow.R
import com.gigo.storyflow.ui.theme.AccentPurple
import com.gigo.storyflow.ui.theme.TextLight
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AboutAppCard(
    cardAlpha: Float,
    angle: Float
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
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

        // App Info Card
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
                        Color(0xFF00BCD4),
                        Color(0xFF9C27B0),
                        Color(0xFF00BCD4),
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
                    "Story Flow",
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
    }
} 