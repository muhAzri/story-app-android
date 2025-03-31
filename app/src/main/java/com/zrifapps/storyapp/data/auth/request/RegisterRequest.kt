package com.zrifapps.storyapp.data.auth.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)
