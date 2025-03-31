package com.zrifapps.storyapp.common.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse(
    @Json(name = "error") val error: Boolean?,
    @Json(name = "message") val message: String?,
    val extraData: Map<String, Any?> = emptyMap(),
)
