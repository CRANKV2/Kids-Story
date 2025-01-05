package com.gigo.kidsstorys.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gigo.kidsstorys.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    isDarkTheme: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = "story"
    ) {
        composable("story") {
            StoryScreen(
                navController = navController,
                isDarkTheme = isDarkTheme
            )
        }
        
        composable("readStory/{storyId}") { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId")?.toIntOrNull() ?: -1
            ReadStoryScreen(
                storyId = storyId,
                onBack = { navController.navigateUp() },
                isDarkTheme = isDarkTheme
            )
        }
        
        composable("settings") {
            SettingsScreen(
                navController = navController,
                isDarkTheme = isDarkTheme
            )
        }
        
        composable("chat") {
            ChatScreen(
                navController = navController
            )
        }
    }
}