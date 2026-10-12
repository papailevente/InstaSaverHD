package com.example.instasaverhd.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.instasaverhd.BuildConfig
import com.example.instasaverhd.data.remote.GitHubReleaseService
import com.example.instasaverhd.data.remote.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

data class UpdateInfo(
    val isUpdateAvailable: Boolean = false,
    val latestVersion: String = "",
    val downloadUrl: String = "",
    val releaseNotes: String = ""
)

class UpdateManager(
    private val context: Context,
    private val gitHubService: GitHubReleaseService = NetworkClient.gitHubReleaseService
) {

    suspend fun checkForUpdates(): Result<UpdateInfo> = withContext(Dispatchers.IO) {
        try {
            val response = gitHubService.getLatestRelease()
            if (response.isSuccessful && response.body() != null) {
                val release = response.body()!!
                val latestTag = release.tagName.trimStart('v')
                val currentVersion = BuildConfig.VERSION_NAME.trimStart('v')

                val isNewer = isVersionNewer(latestTag, currentVersion)

                val matchingAsset = release.assets.firstOrNull { it.name.endsWith(".apk", ignoreCase = true) }
                    ?: release.assets.firstOrNull()

                val downloadUrl = matchingAsset?.downloadUrl.orEmpty()

                Result.success(
                    UpdateInfo(
                        isUpdateAvailable = isNewer && downloadUrl.isNotBlank(),
                        latestVersion = latestTag,
                        downloadUrl = downloadUrl,
                        releaseNotes = release.releaseNotes ?: "Bug fixes and performance updates."
                    )
                )
            } else {
                Result.failure(Exception("Failed to check for updates (${response.code()})"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun downloadUpdateApkDirect(
        downloadUrl: String,
        fileName: String,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: context.cacheDir
            val destinationFile = File(downloadsDir, fileName)
            if (destinationFile.exists()) {
                destinationFile.delete()
            }

            val client = OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(downloadUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful || response.body == null) {
                return@withContext Result.failure(Exception("Failed to download update file (HTTP ${response.code})"))
            }

            val body = response.body!!
            val totalBytes = body.contentLength()
            var bytesDownloaded = 0L

            body.byteStream().use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    val buffer = ByteArray(16 * 1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        bytesDownloaded += bytesRead
                        if (totalBytes > 0) {
                            val progress = ((bytesDownloaded * 100) / totalBytes).toInt()
                            onProgress(progress)
                        }
                    }
                }
            }

            Result.success(destinationFile)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun installApkFromFile(file: File) {
        val apkUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    companion object {
        fun isVersionNewer(latestVersion: String, currentVersion: String): Boolean {
            val latestParts = latestVersion.split(".").mapNotNull { it.toIntOrNull() }
            val currentParts = currentVersion.split(".").mapNotNull { it.toIntOrNull() }

            val length = maxOf(latestParts.size, currentParts.size)
            for (i in 0 until length) {
                val latestPart = latestParts.getOrElse(i) { 0 }
                val currentPart = currentParts.getOrElse(i) { 0 }
                if (latestPart > currentPart) return true
                if (latestPart < currentPart) return false
            }
            return false
        }
    }
}
