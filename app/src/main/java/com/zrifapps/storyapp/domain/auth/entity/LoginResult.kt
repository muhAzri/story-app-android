package com.zrifapps.storyapp.domain.auth.entity

data class LoginResult(
    val userId: String,
    val name: String,
    val token: String,
)
