package com.zrifapps.storyapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zrifapps.storyapp.presentation.screens.auth.LoginScreen
import com.zrifapps.storyapp.presentation.screens.auth.OnboardingScreen
import com.zrifapps.storyapp.presentation.screens.auth.RegisterScreen

@Composable
fun AppRouter(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String,
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
                onLoginSuccess = {}
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
                onRegisterSuccess = { }
            )
        }
    }
}

