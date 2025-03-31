package com.zrifapps.storyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.zrifapps.storyapp.common.navigation.AppRouter
import com.zrifapps.storyapp.common.navigation.AppRoutes
import com.zrifapps.storyapp.common.session.SessionManager
import com.zrifapps.storyapp.ui.theme.StoryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val startDestination = if (sessionManager.getAuthToken().isNullOrBlank()) {
            AppRoutes.OnboardingRoute.route
        } else {
            AppRoutes.HomeRoute.route
        }

        setContent {
            StoryAppTheme {
                AppRouter(
                    navController = rememberNavController(),
                    startDestination = startDestination
                )
            }
        }
    }
}

