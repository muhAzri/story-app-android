package com.zrifapps.storyapp.data.story.response

import com.zrifapps.storyapp.domain.story.entity.Story

data class StoryDetailResponse(
    val error: Boolean,
    val message: String,
    val story: Story,
)
