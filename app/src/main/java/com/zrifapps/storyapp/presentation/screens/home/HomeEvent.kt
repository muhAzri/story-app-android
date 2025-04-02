package com.zrifapps.storyapp.presentation.screens.home

sealed class HomeEvent {
    data object RefreshStories : HomeEvent()
    data class NavigateToStoryDetail(val storyId: String) : HomeEvent()
    data object NavigateToAddStory : HomeEvent()
}
