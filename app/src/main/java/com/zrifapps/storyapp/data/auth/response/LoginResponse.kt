package com.zrifapps.storyapp.data.auth.response

import com.zrifapps.storyapp.domain.auth.entity.LoginResult

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult,
)
