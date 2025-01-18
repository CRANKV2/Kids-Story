package com.gigo.kidsstorys.navigation

import com.gigo.kidsstorys.ui.screens.OnboardingScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gigo.kidsstorys.ui.screens.*

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