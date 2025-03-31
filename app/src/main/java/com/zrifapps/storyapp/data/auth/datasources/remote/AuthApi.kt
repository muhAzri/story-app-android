package com.zrifapps.storyapp.data.auth.datasources.remote

import com.zrifapps.storyapp.data.auth.request.LoginRequest
import com.zrifapps.storyapp.data.auth.request.RegisterRequest
import com.zrifapps.storyapp.data.auth.response.LoginResponse
import com.zrifapps.storyapp.data.auth.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}
