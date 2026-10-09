package com.example.instasaverhd

import com.example.instasaverhd.data.local.DownloadItem
import com.example.instasaverhd.data.local.DownloadStatus
import com.example.instasaverhd.data.repository.DownloadRepository
import com.example.instasaverhd.data.repository.MediaMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeDownloadRepository : DownloadRepository {
    private val downloadsList = mutableListOf<DownloadItem>()
    private val _allDownloads = MutableStateFlow<List<DownloadItem>>(emptyList())
    override val allDownloads: Flow<List<DownloadItem>> = _allDownloads.asStateFlow()

    var shouldReturnError = false
    var errorMessage = "Fake error"

    override suspend fun getDownloadById(id: Long): DownloadItem? {
        return downloadsList.find { it.id == id }
    }

    override suspend fun fetchMediaMetadata(url: String): Result<MediaMetadata> {
        if (shouldReturnError) {
            return Result.failure(Exception(errorMessage))
        }
        return Result.success(
            MediaMetadata(
                originalUrl = url,
                streamUrl = "https://stream.cobalt.tools/test_video.mp4",
                title = "Test Reel",
                thumbnail = "https://stream.cobalt.tools/test_thumb.jpg",
                filename = "test_video.mp4"
            )
        )
    }

    override suspend fun startDownload(
        originalUrl: String,
        streamUrl: String,
        title: String,
        thumbnail: String?
    ): Long {
        val newItem = DownloadItem(
            id = (downloadsList.size + 1).toLong(),
            originalUrl = originalUrl,
            streamUrl = streamUrl,
            title = title,
            thumbnail = thumbnail,
            status = DownloadStatus.DOWNLOADING
        )
        downloadsList.add(newItem)
        _allDownloads.value = downloadsList.toList()
        return newItem.id
    }

    override suspend fun syncDownloadProgress(itemId: Long): DownloadItem? {
        val item = getDownloadById(itemId) ?: return null
        val updated = item.copy(status = DownloadStatus.COMPLETED)
        val index = downloadsList.indexOfFirst { it.id == itemId }
        if (index != -1) {
            downloadsList[index] = updated
            _allDownloads.value = downloadsList.toList()
        }
        return updated
    }

    override suspend fun cancelDownload(itemId: Long) {
        val item = getDownloadById(itemId) ?: return
        val updated = item.copy(status = DownloadStatus.CANCELLED)
        val index = downloadsList.indexOfFirst { it.id == itemId }
        if (index != -1) {
            downloadsList[index] = updated
            _allDownloads.value = downloadsList.toList()
        }
    }

    override suspend fun deleteDownload(item: DownloadItem) {
        downloadsList.removeAll { it.id == item.id }
        _allDownloads.value = downloadsList.toList()
    }

    override suspend fun deleteDownloadById(id: Long) {
        downloadsList.removeAll { it.id == id }
        _allDownloads.value = downloadsList.toList()
    }
}
