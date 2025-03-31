package com.zrifapps.storyapp.data.auth.repository

import com.zrifapps.storyapp.common.network.BaseRepository
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.auth.datasources.remote.AuthApi
import com.zrifapps.storyapp.data.auth.request.LoginRequest
import com.zrifapps.storyapp.data.auth.request.RegisterRequest
import com.zrifapps.storyapp.data.auth.response.LoginResponse
import com.zrifapps.storyapp.data.auth.response.RegisterResponse
import com.zrifapps.storyapp.domain.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : BaseRepository(), AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): NetworkResult<LoginResponse> {
        return safeApiCall { authApi.login(loginRequest) }
    }

    override suspend fun register(registerRequest: RegisterRequest): NetworkResult<RegisterResponse> {
        return safeApiCall { authApi.register(registerRequest) }
    }
}
