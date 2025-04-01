package com.zrifapps.storyapp.presentation.screens.story.create

import android.net.Uri

data class CreateStoryState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val photoUri: Uri? = null,
)
