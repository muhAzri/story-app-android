package com.zrifapps.storyapp.common.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data object EmptySuccess : NetworkResult<Nothing>()
    data class Error(
        val error: Boolean? = null,
        val message: String? = null,
    ) : NetworkResult<Nothing>()
}
