package com.example.instasaverhd.data.repository

import android.app.DownloadManager
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
import java.io.File

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

                val assetKeyword = if (BuildConfig.IS_PRO_VERSION) "pro" else "standard"
                val matchingAsset = release.assets.firstOrNull { it.name.contains(assetKeyword, ignoreCase = true) }
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

    fun downloadUpdateApk(downloadUrl: String, fileName: String, onEnqueue: (Long) -> Unit) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(downloadUrl)

        val request = DownloadManager.Request(uri).apply {
            setTitle("Downloading InstaSaver HD Update")
            setDescription("Downloading version update APK")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            setMimeType("application/vnd.android.package-archive")
        }

        val downloadId = downloadManager.enqueue(request)
        onEnqueue(downloadId)
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
