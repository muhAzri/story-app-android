package com.zrifapps.storyapp.data.story.datasources.remote

import com.zrifapps.storyapp.data.story.response.CreateStoryResponse
import com.zrifapps.storyapp.data.story.response.StoriesResponse
import com.zrifapps.storyapp.data.story.response.StoryDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryApi {
    @GET("stories")
    suspend fun stories(@Query("page") page: Int): Response<StoriesResponse>

    @GET("stories/{id}")
    suspend fun getStoryById(
        @Path("id") id: String,
    ): Response<StoryDetailResponse>


    @Multipart
    @POST("stories")
    suspend fun createStory(
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
        @Part photo: MultipartBody.Part,
    ): Response<CreateStoryResponse>


}
