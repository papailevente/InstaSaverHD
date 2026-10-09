package com.example.instasaverhd.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_items")
data class DownloadItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originalUrl: String,
    val streamUrl: String? = null,
    val title: String = "",
    val thumbnail: String? = null,
    val localUri: String? = null,
    val fileSizeBytes: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val status: DownloadStatus = DownloadStatus.PENDING,
    val downloadId: Long? = null,
    val filename: String? = null
)
