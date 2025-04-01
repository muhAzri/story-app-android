package com.zrifapps.storyapp.presentation.screens.story.detail

sealed class StoryDetailEvent {
    data class LoadStory(val storyId: String) : StoryDetailEvent()
    data object ShareStory : StoryDetailEvent()
    data object OpenMap : StoryDetailEvent()
}
