package com.gigo.storyflow.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gigo.storyflow.ui.screens.AboutScreen
import com.gigo.storyflow.ui.screens.ChatScreen
import com.gigo.storyflow.ui.screens.OnboardingScreen
import com.gigo.storyflow.ui.screens.ReadStoryScreen
import com.gigo.storyflow.ui.screens.SettingsScreen
import com.gigo.storyflow.ui.screens.SplashScreen
import com.gigo.storyflow.ui.screens.StoryScreen

@RequiresApi(Build.VERSION_CODES.P)
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
                navController = navController
            )
        }
        
        composable("readStory/{storyId}") { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId")?.toIntOrNull() ?: -1
            ReadStoryScreen(
                storyId = storyId,
                onBack = { navController.navigateUp() }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                navController = navController
            )
        }
        
        composable("chat") {
            ChatScreen(
                navController = navController
            )
        }

        // In deiner Navigation
        composable("about") {
            AboutScreen(
                navController = navController
            )
        }
    }
}