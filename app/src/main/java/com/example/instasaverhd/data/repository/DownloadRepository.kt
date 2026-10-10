package com.example.instasaverhd.data.repository

import android.content.Context
import com.example.instasaverhd.data.local.AppDatabase
import com.example.instasaverhd.data.local.DownloadDao
import com.example.instasaverhd.data.local.DownloadItem
import com.example.instasaverhd.data.local.DownloadStatus
import com.example.instasaverhd.data.remote.CobaltApiService
import com.example.instasaverhd.data.remote.CobaltPickerItem
import com.example.instasaverhd.data.remote.CobaltRequest
import com.example.instasaverhd.data.remote.NetworkClient
import com.example.instasaverhd.utils.InstagramUrlParser
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MediaMetadata(
    val originalUrl: String,
    val streamUrl: String,
    val title: String,
    val thumbnail: String?,
    val filename: String,
    val pickerItems: List<CobaltPickerItem> = emptyList()
)

interface DownloadRepository {
    val allDownloads: Flow<List<DownloadItem>>
    suspend fun getDownloadById(id: Long): DownloadItem?
    suspend fun fetchMediaMetadata(url: String): Result<MediaMetadata>
    suspend fun startDownload(originalUrl: String, streamUrl: String, title: String, thumbnail: String?): Long
    suspend fun syncDownloadProgress(itemId: Long): DownloadItem?
    suspend fun cancelDownload(itemId: Long)
    suspend fun deleteDownload(item: DownloadItem)
    suspend fun deleteDownloadById(id: Long)
}

class DownloadRepositoryImpl(
    private val downloadDao: DownloadDao,
    private val cobaltApiService: CobaltApiService,
    private val downloadManagerHelper: DownloadManagerHelper
) : DownloadRepository {

    constructor(context: Context) : this(
        downloadDao = AppDatabase.getDatabase(context).downloadDao(),
        cobaltApiService = NetworkClient.cobaltApiService,
        downloadManagerHelper = DownloadManagerHelper(context)
    )

    override val allDownloads: Flow<List<DownloadItem>> = downloadDao.getAllDownloads()

    override suspend fun getDownloadById(id: Long): DownloadItem? {
        return downloadDao.getDownloadById(id)
    }

    override suspend fun fetchMediaMetadata(url: String): Result<MediaMetadata> {
        val cleanedUrl = InstagramUrlParser.cleanUrl(url)
        if (!InstagramUrlParser.isValidUrl(cleanedUrl)) {
            return Result.failure(IllegalArgumentException("Invalid URL format. Please enter a valid Instagram link."))
        }

        val request = CobaltRequest(url = cleanedUrl)
        val endpoints = listOf(
            "https://api.cobalt.tools/",
            "https://cobalt.api.scraye.com/",
            "https://co.wuk.sh/"
        )

        var lastErrorMessage = "Unable to process Instagram link."

        for ((index, endpoint) in endpoints.withIndex()) {
            try {
                val response = if (index == 0) {
                    cobaltApiService.fetchMedia(request)
                } else {
                    cobaltApiService.fetchMediaCustomUrl(endpoint, request)
                }

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    when (body.status) {
                        "tunnel", "redirect", "stream" -> {
                            val streamUrl = body.url
                            if (!streamUrl.isNullOrBlank()) {
                                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                val defaultTitle = "InstaSaver_$timeStamp"
                                val filename = body.filename ?: "$defaultTitle.mp4"

                                return Result.success(
                                    MediaMetadata(
                                        originalUrl = cleanedUrl,
                                        streamUrl = streamUrl,
                                        title = defaultTitle,
                                        thumbnail = null,
                                        filename = filename,
                                        pickerItems = emptyList()
                                    )
                                )
                            }
                        }
                        "picker" -> {
                            val picker = body.picker
                            if (!picker.isNullOrEmpty()) {
                                val firstVideo = picker.firstOrNull { it.type == "video" } ?: picker.first()
                                val streamUrl = firstVideo.url ?: body.url
                                if (!streamUrl.isNullOrBlank()) {
                                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                    val defaultTitle = "InstaSaver_$timeStamp"
                                    val filename = body.filename ?: "$defaultTitle.mp4"

                                    return Result.success(
                                        MediaMetadata(
                                            originalUrl = cleanedUrl,
                                            streamUrl = streamUrl,
                                            title = defaultTitle,
                                            thumbnail = firstVideo.thumb,
                                            filename = filename,
                                            pickerItems = picker
                                        )
                                    )
                                }
                            }
                        }
                        "error" -> {
                            lastErrorMessage = parseErrorMessage(body.text ?: body.errorDetails?.code)
                        }
                        else -> {
                            if (!body.url.isNullOrBlank()) {
                                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                val defaultTitle = "InstaSaver_$timeStamp"
                                return Result.success(
                                    MediaMetadata(
                                        originalUrl = cleanedUrl,
                                        streamUrl = body.url,
                                        title = defaultTitle,
                                        thumbnail = null,
                                        filename = body.filename ?: "$defaultTitle.mp4",
                                        pickerItems = emptyList()
                                    )
                                )
                            }
                        }
                    }
                } else {
                    val errorBodyStr = response.errorBody()?.string()
                    lastErrorMessage = parseErrorMessage(errorBodyStr, response.code())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                lastErrorMessage = e.localizedMessage ?: "Network connection issue. Please try again."
            }
        }

        return Result.failure(Exception(lastErrorMessage))
    }

    private fun parseErrorMessage(rawError: String?, httpCode: Int? = null): String {
        if (rawError.isNullOrBlank()) {
            return if (httpCode != null) "Server error ($httpCode). Please try again." else "Unable to fetch media from Instagram link."
        }
        return try {
            val jsonObject = org.json.JSONObject(rawError)
            val text = jsonObject.optString("text")
            if (text.isNotBlank()) {
                return text
            }
            val errorObj = jsonObject.optJSONObject("error")
            val code = errorObj?.optString("code")
            if (!code.isNullOrBlank()) {
                return when (code) {
                    "error.api.link.invalid" -> "Invalid or unsupported Instagram link."
                    "error.api.fetch.fail" -> "Unable to fetch media from Instagram. The post may be private."
                    "error.api.rate_limit" -> "Rate limit reached. Please wait a moment and try again."
                    else -> "Instagram media request failed ($code)."
                }
            }
            if (httpCode != null) "Request failed ($httpCode)." else "Unable to process Instagram link."
        } catch (e: Exception) {
            if (rawError.contains("cobalt v7 api has been shut down", ignoreCase = true)) {
                "Service updating to Cobalt v10. Please try again."
            } else if (rawError.startsWith("{") && rawError.endsWith("}")) {
                "Unable to process Instagram link. Please ensure the link is public."
            } else {
                rawError
            }
        }
    }

    override suspend fun startDownload(
        originalUrl: String,
        streamUrl: String,
        title: String,
        thumbnail: String?
    ): Long {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "InstaSaver_$timeStamp.mp4"

        val initialItem = DownloadItem(
            originalUrl = originalUrl,
            streamUrl = streamUrl,
            title = title.ifBlank { "Instagram Video $timeStamp" },
            thumbnail = thumbnail,
            status = DownloadStatus.PENDING,
            filename = filename
        )

        val databaseId = downloadDao.insertDownload(initialItem)

        return try {
            val downloadManagerId = downloadManagerHelper.enqueueDownload(
                streamUrl = streamUrl,
                filename = filename,
                title = initialItem.title
            )

            val updatedItem = initialItem.copy(
                id = databaseId,
                downloadId = downloadManagerId,
                status = DownloadStatus.DOWNLOADING
            )
            downloadDao.updateDownload(updatedItem)
            databaseId
        } catch (e: Exception) {
            e.printStackTrace()
            val failedItem = initialItem.copy(
                id = databaseId,
                status = DownloadStatus.FAILED
            )
            downloadDao.updateDownload(failedItem)
            databaseId
        }
    }

    override suspend fun syncDownloadProgress(itemId: Long): DownloadItem? {
        val item = downloadDao.getDownloadById(itemId) ?: return null
        val downloadId = item.downloadId ?: return item

        val progressInfo = downloadManagerHelper.getDownloadProgress(downloadId)
        val updatedItem = item.copy(
            status = progressInfo.status,
            fileSizeBytes = if (progressInfo.totalBytes > 0) progressInfo.totalBytes else progressInfo.bytesDownloaded,
            localUri = progressInfo.localUri ?: item.localUri
        )

        downloadDao.updateDownload(updatedItem)
        return updatedItem
    }

    override suspend fun cancelDownload(itemId: Long) {
        val item = downloadDao.getDownloadById(itemId) ?: return
        item.downloadId?.let { downloadManagerHelper.cancelDownload(it) }
        val updatedItem = item.copy(status = DownloadStatus.CANCELLED)
        downloadDao.updateDownload(updatedItem)
    }

    override suspend fun deleteDownload(item: DownloadItem) {
        item.downloadId?.let { downloadManagerHelper.cancelDownload(it) }
        downloadManagerHelper.deleteDownloadedFile(item.localUri)
        downloadDao.deleteDownload(item)
    }

    override suspend fun deleteDownloadById(id: Long) {
        val item = downloadDao.getDownloadById(id)
        if (item != null) {
            deleteDownload(item)
        } else {
            downloadDao.deleteDownloadById(id)
        }
    }
}
