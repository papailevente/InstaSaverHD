package com.example.instasaverhd.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download_items ORDER BY timestamp DESC")
    fun getAllDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM download_items WHERE id = :id")
    suspend fun getDownloadById(id: Long): DownloadItem?

    @Query("SELECT * FROM download_items WHERE downloadId = :downloadId")
    suspend fun getDownloadByDownloadId(downloadId: Long): DownloadItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(downloadItem: DownloadItem): Long

    @Update
    suspend fun updateDownload(downloadItem: DownloadItem)

    @Delete
    suspend fun deleteDownload(downloadItem: DownloadItem)

    @Query("DELETE FROM download_items WHERE id = :id")
    suspend fun deleteDownloadById(id: Long)

    @Query("UPDATE download_items SET status = :status, localUri = :localUri, fileSizeBytes = :fileSizeBytes WHERE downloadId = :downloadId")
    suspend fun updateDownloadStatusByDownloadId(downloadId: Long, status: DownloadStatus, localUri: String?, fileSizeBytes: Long)
}
