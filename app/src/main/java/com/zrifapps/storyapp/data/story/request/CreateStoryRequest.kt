package com.zrifapps.storyapp.data.story.request

data class CreateStoryRequest(
    val description: String,
    val lat: Float? = null,
    val lon: Float? = null,
)
