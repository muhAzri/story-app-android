package com.zrifapps.storyapp.data.story.datasources.remote

import com.zrifapps.storyapp.data.story.response.StoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StoryApi {
    @GET("/stories")
    suspend fun stories(@Query("page") page: Int): Response<StoriesResponse>
}
