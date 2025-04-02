package com.zrifapps.storyapp.common.navigation

class DrawerNavigationHandler(
    val onNavigateToHome: () -> Unit,
    val onNavigateToMap: () -> Unit,
    val onLogout: () -> Unit,
) {
    companion object {
        fun create(
            navigateTo: (AppRoutes) -> Unit,
            handleLogout: () -> Unit,
        ): DrawerNavigationHandler {
            return DrawerNavigationHandler(
                onNavigateToHome = { navigateTo(Home) },
                onNavigateToMap = { navigateTo(MapView) },
                onLogout = handleLogout
            )
        }
    }
}
