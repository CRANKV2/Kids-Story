package com.gigo.kidsstorys.navigation

import OnboardingScreen
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
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                isDarkTheme = isDarkTheme,
                onTimeout = {
                    navController.navigate("stories") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onFirstLaunch = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("stories") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("stories") {
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