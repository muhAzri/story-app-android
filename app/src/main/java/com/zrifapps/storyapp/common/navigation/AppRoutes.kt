package com.zrifapps.storyapp.common.navigation

import kotlinx.serialization.Serializable

interface AppRoutes

@Serializable
object Onboarding : AppRoutes

@Serializable
object Login : AppRoutes

@Serializable
object Register : AppRoutes

@Serializable
object Home : AppRoutes

@Serializable
object CreateStory : AppRoutes

@Serializable
data class Story(val storyId: String) : AppRoutes
