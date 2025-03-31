package com.zrifapps.storyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.zrifapps.storyapp.common.navigation.AppRouter
import com.zrifapps.storyapp.common.navigation.AppRoutes
import com.zrifapps.storyapp.ui.theme.StoryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoryAppTheme {
                AppRouter(
                    navController = rememberNavController(),
                    startDestination = AppRoutes.OnboardingRoute.route
                )
            }
        }
    }
}

