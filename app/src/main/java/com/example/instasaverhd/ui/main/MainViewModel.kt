package com.example.instasaverhd.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instasaverhd.data.local.DownloadItem
import com.example.instasaverhd.data.repository.DownloadRepository
import com.example.instasaverhd.data.repository.DownloadRepositoryImpl
import com.example.instasaverhd.data.repository.MediaMetadata
import com.example.instasaverhd.utils.InstagramUrlParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface FetchState {
    object Idle : FetchState
    object Loading : FetchState
    data class Success(val metadata: MediaMetadata) : FetchState
    data class Error(val message: String) : FetchState
}

data class MainUiState(
    val urlInput: String = "",
    val isValidUrl: Boolean = false,
    val isInstagramUrl: Boolean = false,
    val fetchState: FetchState = FetchState.Idle,
    val activePreviewMetadata: MediaMetadata? = null,
    val downloadsList: List<DownloadItem> = emptyList(),
    val isDownloading: Boolean = false,
    val snackbarMessage: String? = null
)

class MainViewModel(
    private val repository: DownloadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeDownloads()
    }

    private fun observeDownloads() {
        viewModelScope.launch {
            repository.allDownloads.collect { downloads ->
                _uiState.update { currentState ->
                    currentState.copy(downloadsList = downloads)
                }
            }
        }
    }

    fun onUrlChanged(newUrl: String) {
        val valid = InstagramUrlParser.isValidUrl(newUrl)
        val isInsta = InstagramUrlParser.isInstagramUrl(newUrl)
        _uiState.update {
            it.copy(
                urlInput = newUrl,
                isValidUrl = valid,
                isInstagramUrl = isInsta,
                fetchState = if (it.fetchState is FetchState.Error) FetchState.Idle else it.fetchState
            )
        }
    }

    fun onPasteFromClipboard(clipboardText: String) {
        val trimmed = clipboardText.trim()
        if (trimmed.isNotEmpty()) {
            onUrlChanged(trimmed)
            if (InstagramUrlParser.isValidUrl(trimmed)) {
                fetchMedia()
            }
        }
    }

    fun onClearInput() {
        _uiState.update {
            it.copy(
                urlInput = "",
                isValidUrl = false,
                isInstagramUrl = false,
                fetchState = FetchState.Idle,
                activePreviewMetadata = null
            )
        }
    }

    fun fetchMedia() {
        val currentUrl = _uiState.value.urlInput
        if (!InstagramUrlParser.isValidUrl(currentUrl)) {
            _uiState.update {
                it.copy(
                    fetchState = FetchState.Error("Please paste a valid Instagram link (e.g., https://www.instagram.com/reel/...)")
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(fetchState = FetchState.Loading) }

            val result = repository.fetchMediaMetadata(currentUrl)
            result.fold(
                onSuccess = { metadata ->
                    _uiState.update {
                        it.copy(
                            fetchState = FetchState.Success(metadata),
                            activePreviewMetadata = metadata,
                            snackbarMessage = "Media link extracted successfully!"
                        )
                    }
                },
                onFailure = { error ->
                    val errorMsg = error.localizedMessage ?: "Failed to extract Instagram video URL."
                    _uiState.update {
                        it.copy(
                            fetchState = FetchState.Error(errorMsg),
                            snackbarMessage = errorMsg
                        )
                    }
                }
            )
        }
    }

    fun startDownload(metadata: MediaMetadata) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloading = true) }
            try {
                val dbId = repository.startDownload(
                    originalUrl = metadata.originalUrl,
                    streamUrl = metadata.streamUrl,
                    title = metadata.title,
                    thumbnail = metadata.thumbnail
                )
                _uiState.update {
                    it.copy(
                        isDownloading = false,
                        snackbarMessage = "Download started! Check progress in Gallery/History."
                    )
                }
                repository.syncDownloadProgress(dbId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isDownloading = false,
                        snackbarMessage = "Failed to start download: ${e.message}"
                    )
                }
            }
        }
    }

    fun cancelDownload(itemId: Long) {
        viewModelScope.launch {
            repository.cancelDownload(itemId)
        }
    }

    fun deleteDownload(item: DownloadItem) {
        viewModelScope.launch {
            repository.deleteDownload(item)
            _uiState.update {
                it.copy(snackbarMessage = "Download deleted.")
            }
        }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                val repository = DownloadRepositoryImpl(context.applicationContext)
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
