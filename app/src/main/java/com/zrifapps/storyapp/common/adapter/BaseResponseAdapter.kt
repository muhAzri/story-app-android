package com.zrifapps.storyapp.common.adapter


import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import com.zrifapps.storyapp.common.network.BaseResponse

class BaseResponseAdapter : JsonAdapter<BaseResponse>() {

    @FromJson
    override fun fromJson(reader: JsonReader): BaseResponse {
        var error: Boolean? = null
        var message: String? = null
        val extraData: MutableMap<String, Any?> = mutableMapOf()

        reader.beginObject()
        while (reader.hasNext()) {
            when (val name = reader.nextName()) {
                "error" -> error = reader.nextBoolean()
                "message" -> message = reader.nextString()
                else -> extraData[name] = reader.readJsonValue()
            }
        }
        reader.endObject()

        return BaseResponse(error, message, extraData)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: BaseResponse?) {
        writer.beginObject()
        writer.name("error").value(value?.error)
        writer.name("message").value(value?.message)
        value?.extraData?.forEach { (key, jsonValue) ->
            writer.name(key)
            writer.jsonValue(jsonValue)
        }
        writer.endObject()
    }
}
