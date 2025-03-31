package com.zrifapps.storyapp.common.navigation

sealed class AppRoutes(val route: String) {
    data object OnboardingRoute : AppRoutes(route = "onboarding")
    data object LoginRoute : AppRoutes(route = "login")
    data object RegisterRoute : AppRoutes(route = "register")
}
