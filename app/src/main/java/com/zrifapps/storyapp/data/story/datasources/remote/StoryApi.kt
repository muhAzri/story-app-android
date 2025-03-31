package com.zrifapps.storyapp.data.story.datasources.remote

import com.zrifapps.storyapp.data.story.response.StoriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface StoryApi {
    @GET("/stories")
    suspend fun stories(): Response<StoriesResponse>
}
