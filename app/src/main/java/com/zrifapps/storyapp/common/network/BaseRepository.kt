package com.zrifapps.storyapp.common.network

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<BaseResponse>,
    ): NetworkResult<T> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                if (body.error == false) {
                    // Extract the first key from `extraData`
                    val extractedData: Any? = body.extraData.values.firstOrNull()

                    if (extractedData != null) {
                        @Suppress("UNCHECKED_CAST")
                        NetworkResult.Success(extractedData as T)
                    } else {
                        NetworkResult.EmptySuccess
                    }
                } else {
                    NetworkResult.Error(
                        error = true,
                        message = body.message ?: "Unknown API error"
                    )
                }
            } else {
                NetworkResult.Error(
                    error = true,
                    message = body?.message ?: response.message()
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
