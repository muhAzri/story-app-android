package com.zrifapps.storyapp.presentation.screens.story.create


import android.net.Uri

sealed class CreateStoryEvent {
    data class SetPhotoUri(val uri: Uri) : CreateStoryEvent()
    data class CreateStory(val description: String) : CreateStoryEvent()
    data object ClearError : CreateStoryEvent()
}
