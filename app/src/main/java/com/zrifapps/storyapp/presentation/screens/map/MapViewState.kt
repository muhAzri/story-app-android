package com.zrifapps.storyapp.presentation.screens.map

import com.zrifapps.storyapp.domain.story.entity.Story

data class MapViewState(
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
