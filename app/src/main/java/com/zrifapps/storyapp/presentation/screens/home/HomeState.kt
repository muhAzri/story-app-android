package com.zrifapps.storyapp.presentation.screens.home

import com.zrifapps.storyapp.domain.story.entity.Story

data class HomeState(
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
)
