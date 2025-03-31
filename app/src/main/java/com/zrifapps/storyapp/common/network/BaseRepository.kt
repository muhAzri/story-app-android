package com.zrifapps.storyapp.common.network

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
    ): NetworkResult<T> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body)
            } else {
                NetworkResult.Error(
                    error = true,
                    message = response.message()
                )
            }
        } catch (e: HttpException) {
            NetworkResult.Error(
                error = true,
                message = e.message()
            )
        } catch (e: IOException) {
            NetworkResult.Error(
                error = true,
                message = "Network error, please check your connection."
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                error = true,
                message = e.localizedMessage ?: "Unknown error occurred"
            )
        }
    }
}
