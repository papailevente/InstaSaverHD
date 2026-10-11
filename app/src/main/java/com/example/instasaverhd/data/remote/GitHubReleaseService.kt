package com.example.instasaverhd.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

@JsonClass(generateAdapter = true)
data class GitHubAsset(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "browser_download_url") val downloadUrl: String,
    @field:Json(name = "size") val sizeBytes: Long = 0
)

@JsonClass(generateAdapter = true)
data class GitHubReleaseResponse(
    @field:Json(name = "tag_name") val tagName: String,
    @field:Json(name = "name") val releaseName: String? = null,
    @field:Json(name = "body") val releaseNotes: String? = null,
    @field:Json(name = "assets") val assets: List<GitHubAsset> = emptyList()
)

interface GitHubReleaseService {
    @Headers(
        "Accept: application/vnd.github+json"
    )
    @GET("repos/papailevente/InstaSaverHD/releases/latest")
    suspend fun getLatestRelease(
        @Header("User-Agent") userAgent: String = "InstaSaverHD/App"
    ): Response<GitHubReleaseResponse>
}
