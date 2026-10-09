package com.example.instasaverhd.data.repository

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import com.example.instasaverhd.data.local.DownloadStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

data class DownloadProgressInfo(
    val downloadId: Long,
    val status: DownloadStatus,
    val bytesDownloaded: Long,
    val totalBytes: Long,
    val localUri: String? = null
)

class DownloadManagerHelper(private val context: Context) {
    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun enqueueDownload(streamUrl: String, filename: String, title: String): Long {
        val request = DownloadManager.Request(Uri.parse(streamUrl)).apply {
            setTitle(title.ifBlank { filename })
            setDescription("Downloading Instagram media with InstaSaver HD")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        return downloadManager.enqueue(request)
    }

    fun getDownloadProgress(downloadId: Long): DownloadProgressInfo {
        val query = DownloadManager.Query().setFilterById(downloadId)
        var cursor: Cursor? = null
        try {
            cursor = downloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val totalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)

                val dmStatus = if (statusIndex >= 0) cursor.getInt(statusIndex) else -1
                val downloaded = if (downloadedIndex >= 0) cursor.getLong(downloadedIndex) else 0L
                val total = if (totalIndex >= 0) cursor.getLong(totalIndex) else 0L
                val uriString = if (uriIndex >= 0) cursor.getString(uriIndex) else null

                val status = when (dmStatus) {
                    DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.COMPLETED
                    DownloadManager.STATUS_FAILED -> DownloadStatus.FAILED
                    DownloadManager.STATUS_RUNNING -> DownloadStatus.DOWNLOADING
                    DownloadManager.STATUS_PENDING, DownloadManager.STATUS_PAUSED -> DownloadStatus.PENDING
                    else -> DownloadStatus.PENDING
                }

                return DownloadProgressInfo(
                    downloadId = downloadId,
                    status = status,
                    bytesDownloaded = downloaded,
                    totalBytes = total,
                    localUri = uriString
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return DownloadProgressInfo(downloadId, DownloadStatus.FAILED, 0L, 0L, null)
    }

    fun trackProgressFlow(downloadId: Long): Flow<DownloadProgressInfo> = flow {
        var isFinished = false
        while (!isFinished) {
            val info = getDownloadProgress(downloadId)
            emit(info)
            if (info.status == DownloadStatus.COMPLETED || info.status == DownloadStatus.FAILED) {
                isFinished = true
            } else {
                delay(500)
            }
        }
    }

    fun cancelDownload(downloadId: Long) {
        try {
            downloadManager.remove(downloadId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDownloadedFile(localUriString: String?): Boolean {
        if (localUriString == null) return false
        return try {
            val uri = Uri.parse(localUriString)
            if (uri.scheme == "file") {
                val file = File(uri.path ?: return false)
                if (file.exists()) file.delete() else false
            } else {
                context.contentResolver.delete(uri, null, null) > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
