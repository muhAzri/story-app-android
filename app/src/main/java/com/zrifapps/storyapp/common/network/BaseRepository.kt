package com.zrifapps.storyapp.common.network

import org.json.JSONObject
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
                val errorMessage = parseError(response)
                NetworkResult.Error(
                    error = true, message = errorMessage
                )
            }
        } catch (e: HttpException) {
            NetworkResult.Error(
                error = true, message = e.message()
            )
        } catch (e: IOException) {
            NetworkResult.Error(
                error = true, message = "Network error, please check your connection."
            )
        } catch (e: Exception) {
            NetworkResult.Error(
                error = true, message = e.localizedMessage ?: "Unknown error occurred"
            )
        }
    }

    private fun parseError(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "Unknown error")
            } else {
                "Unknown error"
            }
        } catch (e: Exception) {
            "Failed to parse error response"
        }
    }
}
