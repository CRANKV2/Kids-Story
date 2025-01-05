package com.gigo.kidsstorys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gigo.kidsstorys.ui.screens.*
import com.gigo.kidsstorys.ui.theme.TestappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.black)
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    var isDarkMode by remember { mutableStateOf(true) }
    
    TestappTheme {
        val navController = rememberNavController()
        NavHost(
            modifier = Modifier.background(Color(0xFF1E1E2A)),
            navController = navController, 
            startDestination = "stories"
        ) {
            composable("stories") {
                StoryScreen(
                    isDarkTheme = isDarkMode,
                    navController = navController
                )
            }
            composable(
                route = "readStory/{storyId}",
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) +
                    scaleIn(
                        animationSpec = tween(300),
                        initialScale = 0.8f
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300))
                }
            ) { backStackEntry ->
                val storyId = backStackEntry.arguments?.getString("storyId")?.toIntOrNull() ?: return@composable
                ReadStoryScreen(
                    storyId = storyId,
                    onBack = { navController.navigateUp() },
                    isDarkTheme = isDarkMode
                )
            }
            composable(
                route = "settings",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                SettingsScreen(
                    navController = navController,
                    isDarkTheme = isDarkMode
                )
            }
        }
    }
}