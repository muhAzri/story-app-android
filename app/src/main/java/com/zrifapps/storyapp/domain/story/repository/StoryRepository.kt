package com.zrifapps.storyapp.domain.story.repository

import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.data.story.response.StoriesResponse
import com.zrifapps.storyapp.data.story.response.StoryDetailResponse

interface StoryRepository {
    suspend fun stories(getStoryRequest: GetStoryRequest): NetworkResult<StoriesResponse>
    suspend fun getStoryById(id: String): NetworkResult<StoryDetailResponse>
}

