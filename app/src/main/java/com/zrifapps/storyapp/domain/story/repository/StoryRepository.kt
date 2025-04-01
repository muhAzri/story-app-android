package com.zrifapps.storyapp.domain.story.repository

import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.data.story.response.StoriesResponse

interface StoryRepository {
    suspend fun stories(getStoryRequest: GetStoryRequest): NetworkResult<StoriesResponse>
}

