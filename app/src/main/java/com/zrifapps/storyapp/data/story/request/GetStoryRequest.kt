package com.zrifapps.storyapp.data.story.request

data class GetStoryRequest(
    val page: Int,
    val location: Int = 0,
)
