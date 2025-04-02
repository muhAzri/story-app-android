package com.zrifapps.storyapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
    startDestination: AppRoutes,
    sessionManager: SessionManager,
) {

    fun navigateTo(route: AppRoutes, clearBackStack: Boolean = false) {
        navController.navigate(route) {
            if (clearBackStack) {
                popUpTo(navController.graph.startDestinationRoute ?: "") { inclusive = true }
            }
        }
    }

    fun handleLogout() {
        sessionManager.clearSession()
        navController.navigate(Login) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Onboarding> {
            OnboardingScreen(
                onFinishOnboarding = { navigateTo(Login) }
            )
        }

        composable<Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navigateTo(Register, clearBackStack = true)
                },
                onLoginSuccess = {
                    navigateTo(Home, clearBackStack = true)
                }
            )
        }

        composable<Register> {
            RegisterScreen(
                onNavigateToLogin = {
                    navigateTo(Login, clearBackStack = true)
                },
            )
        }

        composable<Home> {
            HomeScreen(
                onLogout = {
                    handleLogout()
                },
                onNavigateToHome = {
                    navigateTo(Home)
                },
                onNavigateToStoryDetail = { storyId ->
                    navigateTo(Story(storyId))
                },
                onNavigateToAddStory = {
                    navigateTo(CreateStory)
                },
            )
        }

        composable<Story> { backStackEntry ->
            val story = backStackEntry.toRoute<Story>()
            StoryDetailScreen(
                storyId = story.storyId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CreateStory> {
            CreateStoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
