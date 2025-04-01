package com.zrifapps.storyapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zrifapps.storyapp.common.session.SessionManager
import com.zrifapps.storyapp.presentation.screens.auth.login.LoginScreen
import com.zrifapps.storyapp.presentation.screens.auth.onboarding.OnboardingScreen
import com.zrifapps.storyapp.presentation.screens.auth.register.RegisterScreen
import com.zrifapps.storyapp.presentation.screens.home.HomeScreen
import com.zrifapps.storyapp.presentation.screens.story.create.CreateStoryScreen
import com.zrifapps.storyapp.presentation.screens.story.detail.StoryDetailScreen

@Composable
fun AppRouter(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String,
    sessionManager: SessionManager,
) {
    fun navigateTo(route: String, clearBackStack: Boolean = false) {
        navController.navigate(route) {
            if (clearBackStack) {
                popUpTo(AppRoutes.OnboardingRoute.route) { inclusive = true }
            } else {
                popUpTo(route) {
                    inclusive = true
                }
            }
        }
    }

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(AppRoutes.OnboardingRoute.route) {
            OnboardingScreen(
                onFinishOnboarding = { navigateTo(AppRoutes.LoginRoute.route) }
            )
        }

        composable(AppRoutes.LoginRoute.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navigateTo(
                        AppRoutes.RegisterRoute.route,
                        clearBackStack = true
                    )
                },
                onLoginSuccess = {
                    navigateTo(
                        AppRoutes.HomeRoute.route,
                        clearBackStack = true
                    )
                }
            )
        }

        composable(AppRoutes.RegisterRoute.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navigateTo(
                        AppRoutes.LoginRoute.route,
                        clearBackStack = true
                    )
                },
            )
        }

        composable(AppRoutes.HomeRoute.route) {
            HomeScreen(
                onLogout = {
                    sessionManager.clearSession()
                    navigateTo(AppRoutes.LoginRoute.route, clearBackStack = true)
                },
                onNavigateToHome = {
                    navigateTo(AppRoutes.HomeRoute.route, clearBackStack = true)
                },
                onNavigateToStoryDetail = { storyId ->
                    navigateTo(AppRoutes.StoryRoute.createRoute(storyId))
                },
                onNavigateToAddStory = {
                    navigateTo(AppRoutes.CreateStoryRoute.route)
                },
            )
        }

        composable(AppRoutes.StoryRoute.route) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: return@composable
            StoryDetailScreen(storyId = storyId, onNavigateBack = { navController.popBackStack() })
        }

        composable(AppRoutes.CreateStoryRoute.route) {
            CreateStoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}

