package com.gigo.storyflow

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.gigo.storyflow.navigation.AppNavigation
import com.gigo.storyflow.ui.theme.StoryFlowTheme
import com.gigo.storyflow.ui.utils.SystemUiController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // System UI ausblenden
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            val isDarkTheme by remember { mutableStateOf(true) }
            
            SystemUiController(window).also { controller ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    controller.isSystemBarsVisible = false
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
            
            StoryFlowTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

