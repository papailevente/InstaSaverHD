package com.example.instasaverhd.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface CobaltApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/json")
    suspend fun fetchMedia(
        @Body request: CobaltRequest,
        @Header("User-Agent") userAgent: String = "InstaSaverHD/1.0 (Android)"
    ): Response<CobaltResponse>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST
    suspend fun fetchMediaCustomUrl(
        @Url endpointUrl: String,
        @Body request: CobaltRequest,
        @Header("User-Agent") userAgent: String = "InstaSaverHD/1.0 (Android)"
    ): Response<CobaltResponse>
}
