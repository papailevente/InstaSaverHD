package com.example.instasaverhd.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CobaltRequest(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "videoQuality") val videoQuality: String = "max",
    @field:Json(name = "downloadMode") val downloadMode: String = "auto",
    @field:Json(name = "youtubeVideoCodec") val youtubeVideoCodec: String = "h264"
)

@JsonClass(generateAdapter = true)
data class CobaltPickerItem(
    @field:Json(name = "type") val type: String? = null,
    @field:Json(name = "url") val url: String? = null,
    @field:Json(name = "thumb") val thumb: String? = null
)

@JsonClass(generateAdapter = true)
data class CobaltErrorDetails(
    @field:Json(name = "code") val code: String? = null,
    @field:Json(name = "context") val context: String? = null
)

@JsonClass(generateAdapter = true)
data class CobaltResponse(
    @field:Json(name = "status") val status: String,
    @field:Json(name = "url") val url: String? = null,
    @field:Json(name = "filename") val filename: String? = null,
    @field:Json(name = "picker") val picker: List<CobaltPickerItem>? = null,
    @field:Json(name = "pickerType") val pickerType: String? = null,
    @field:Json(name = "audio") val audio: String? = null,
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "error") val errorDetails: CobaltErrorDetails? = null
)
