package com.zrifapps.storyapp.data.story.repository

import com.zrifapps.storyapp.common.network.BaseRepository
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.datasources.remote.StoryApi
import com.zrifapps.storyapp.data.story.request.CreateStoryRequest
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.data.story.response.CreateStoryResponse
import com.zrifapps.storyapp.data.story.response.StoriesResponse
import com.zrifapps.storyapp.data.story.response.StoryDetailResponse
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun createStory(
        createStoryRequest: CreateStoryRequest,
        photo: MultipartBody.Part,
    ): NetworkResult<CreateStoryResponse> {
        val description = createStoryRequest.description
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val lat =
            createStoryRequest.lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

        val lon =
            createStoryRequest.lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

        return safeApiCall {
            storyApi.createStory(
                description = description,
                lat = lat,
                lon = lon,
                photo = photo
            )
        }
    }

}
