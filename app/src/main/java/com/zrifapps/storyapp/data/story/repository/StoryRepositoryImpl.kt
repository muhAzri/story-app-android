package com.zrifapps.storyapp.data.story.repository

import com.zrifapps.storyapp.common.network.BaseRepository
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.datasources.remote.StoryApi
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.data.story.response.StoriesResponse
import com.zrifapps.storyapp.data.story.response.StoryDetailResponse
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val storyApi: StoryApi,
) : BaseRepository(), StoryRepository {

    override suspend fun stories(getStoryRequest: GetStoryRequest): NetworkResult<StoriesResponse> {
        return safeApiCall { storyApi.stories(getStoryRequest.page) }
    }

    override suspend fun getStoryById(id: String): NetworkResult<StoryDetailResponse> {
        return safeApiCall { storyApi.getStoryById(id) }
    }
}
