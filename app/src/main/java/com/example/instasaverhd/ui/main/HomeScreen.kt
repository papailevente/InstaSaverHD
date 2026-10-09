package com.example.instasaverhd.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instasaverhd.ui.components.GlassCard
import com.example.instasaverhd.ui.components.InstagramVideoPlayer
import com.example.instasaverhd.ui.components.UrlInputBar
import com.example.instasaverhd.ui.theme.DarkBackground
import com.example.instasaverhd.ui.theme.InstaPink
import com.example.instasaverhd.ui.theme.InstaSaverHDTheme
import com.example.instasaverhd.ui.theme.InstagramGradient

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    HomeScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onUrlChanged = viewModel::onUrlChanged,
        onPasteClicked = viewModel::onPasteFromClipboard,
        onClearClicked = viewModel::onClearInput,
        onFetchClicked = viewModel::fetchMedia,
        onDownloadClick = { metadata -> viewModel.startDownload(metadata) },
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    uiState: MainUiState,
    snackbarHostState: SnackbarHostState,
    onUrlChanged: (String) -> Unit,
    onPasteClicked: (String) -> Unit,
    onClearClicked: () -> Unit,
    onFetchClicked: () -> Unit,
    onDownloadClick: (com.example.instasaverhd.data.repository.MediaMetadata) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Bar Header with Instagram Aesthetic
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(InstagramGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VideoLibrary,
                            contentDescription = "InstaSaver Logo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "InstaSaver HD",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Fast HD Reel & Video Downloader",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // URL Input Component with Single-Tap Paste & Validation
            UrlInputBar(
                urlInput = uiState.urlInput,
                isValidUrl = uiState.isValidUrl,
                isInstagramUrl = uiState.isInstagramUrl,
                isLoading = uiState.fetchState is FetchState.Loading,
                onUrlChanged = onUrlChanged,
                onPasteClicked = onPasteClicked,
                onClearClicked = onClearClicked,
                onFetchClicked = onFetchClicked
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Error Message Display Card
            if (uiState.fetchState is FetchState.Error) {
                val errorMessage = uiState.fetchState.message
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = Color(0xFFEF4444)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ErrorOutline,
                                contentDescription = "Error",
                                tint = Color(0xFFF87171),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Unable to fetch media",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = errorMessage,
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp
                                )
                            }
                            IconButton(onClick = onFetchClicked) {
                                Icon(
                                    imageVector = Icons.Rounded.Refresh,
                                    contentDescription = "Retry",
                                    tint = InstaPink
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Inline ExoPlayer Preview Card
            uiState.activePreviewMetadata?.let { metadata ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    InstagramVideoPlayer(
                        mediaUrl = metadata.streamUrl,
                        title = metadata.title,
                        thumbnailUrl = metadata.thumbnail,
                        onDownloadClick = { onDownloadClick(metadata) },
                        isDownloading = uiState.isDownloading,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomeScreenPreview() {
    InstaSaverHDTheme {
        HomeScreenContent(
            uiState = MainUiState(
                urlInput = "https://www.instagram.com/reel/C123456/",
                isValidUrl = true,
                isInstagramUrl = true,
                fetchState = FetchState.Idle
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUrlChanged = {},
            onPasteClicked = {},
            onClearClicked = {},
            onFetchClicked = {},
            onDownloadClick = {}
        )
    }
}
