package com.zrifapps.storyapp.presentation.screens.story.detail

import com.zrifapps.storyapp.domain.story.entity.Story

data class StoryDetailState(
    val story: Story? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
