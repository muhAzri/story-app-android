package com.zrifapps.storyapp.domain.auth.repository

import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.auth.request.LoginRequest
import com.zrifapps.storyapp.data.auth.request.RegisterRequest
import com.zrifapps.storyapp.data.auth.response.LoginResponse
import com.zrifapps.storyapp.data.auth.response.RegisterResponse

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): NetworkResult<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): NetworkResult<RegisterResponse>
}
