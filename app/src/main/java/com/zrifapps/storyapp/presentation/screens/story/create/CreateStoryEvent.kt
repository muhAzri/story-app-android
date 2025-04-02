package com.zrifapps.storyapp.presentation.screens.story.create


import android.net.Uri

sealed class CreateStoryEvent {
    data class SetPhotoUri(val uri: Uri) : CreateStoryEvent()
    data class CreateStory(val description: String, val lat: Float?, val lon: Float?) :
        CreateStoryEvent()

    data object ClearError : CreateStoryEvent()
}
