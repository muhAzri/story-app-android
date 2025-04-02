package com.zrifapps.storyapp.presentation.screens.map

sealed class MapViewEvent {
    data object LoadStoriesWithLocation : MapViewEvent()
    data class NavigateToStoryDetail(val storyId: String) : MapViewEvent()
}
