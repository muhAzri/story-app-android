package com.zrifapps.storyapp.data.story.response

import com.zrifapps.storyapp.domain.story.entity.Story

data class StoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>,
)
