package com.gigo.kidsstorys

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.gigo.kidsstorys.navigation.AppNavigation
import com.gigo.kidsstorys.ui.theme.KidsStorysTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.black)
        
        setContent {
            val isDarkTheme by remember { mutableStateOf(true) }
            
            KidsStorysTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

