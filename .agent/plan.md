# Project Plan

Create InstaSaver HD Android application that allows users to fetch Instagram reel/video stream URLs using Cobalt API, preview them in-app with Jetpack Media3 ExoPlayer, download them to device storage, and manage downloaded videos in a gallery tab with M3 Instagram gradient aesthetic and edge-to-edge design.

## Project Brief

# Project Brief: InstaSaver HD

## Features
1. **URL Input & Clipboard Integration**: Quick-paste Instagram Reel, Post, or Video links with single-tap clipboard integration and instant URL validation.
2. **Cobalt API & Media Fetcher**: Extract high-definition MP4 direct download URLs and media metadata via Cobalt API JSON endpoints.
3. **In-App Media Preview Player**: Stream and preview fetched Instagram videos inline prior to downloading using Jetpack Media3.
4. **Background Download Manager**: High-speed video downloader using Android DownloadManager with real-time download progress tracking and MediaStore gallery registration.
5. **Saved Downloads Gallery & History**: Dedicated gallery view to list, play, share, or delete downloaded Instagram media files.

## High-Level Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3 (M3) dark theme featuring vibrant Instagram gradient accents (`#833ab4`, `#fd1d1d`, `#fcb045`), glassmorphism cards, and full edge-to-edge display.
- **Navigation & Adaptive Strategy**: Jetpack Navigation 3 (state-driven) and Compose Material Adaptive library for adaptive, flexible layouts across different screen sizes.
- **Networking**: OkHttp & Retrofit with Moshi for interacting with the Cobalt API endpoint.
- **Media Playback**: Jetpack Media3 (ExoPlayer) for inline media preview and playback.
- **Asynchronous Processing**: Kotlin Coroutines and Flow for seamless background processing and reactive UI state updates.
- **Storage & Download Management**: Android System `DownloadManager` for device file acquisition, `MediaStore` API for gallery integration, and `Room` for managing download history persistence.

## Implementation Steps
**Total Duration:** 11m

### Task_1_SetupDataAndNetwork: Set up Room Database for download history, Moshi/Retrofit client for Cobalt API media URL extraction, and DownloadManager / MediaStore repository for background file downloads.
- **Status:** COMPLETED
- **Updates:** Successfully created Room Entity DownloadItem, DAO, AppDatabase, Retrofit Moshi interface for Cobalt API, DownloadManagerHelper, DownloadRepositoryImpl, and InstagramUrlParser. Tested and built app with gradle assembleDebug.
- **Acceptance Criteria:**
  - Room entity, DAO, and Database created for DownloadItem
  - Retrofit interface and Moshi DTOs defined for Cobalt API requests
  - Repository and DownloadManager helper implemented for downloading video files and registering with MediaStore
  - Code compiles successfully without build errors
- **Duration:** 7m 50s

### Task_2_BuildCoreUIAndExoPlayer: Implement Material 3 Instagram gradient theme, Clipboard paste & URL input component, Jetpack Media3 ExoPlayer inline preview player, and MainViewModel for state management.
- **Status:** COMPLETED
- **Updates:** Successfully implemented Material 3 dark theme with Instagram gradient accents and glassmorphism styling, UrlInputBar with clipboard paste, InstagramVideoPlayer with ExoPlayer loop/controls, and MainViewModel managing state transitions. Built and verified with gradlew assembleDebug.
- **Acceptance Criteria:**
  - Vibrant M3 dark theme with Instagram gradient accents and full edge-to-edge support established
  - URL input bar with single-tap clipboard paste button and instant validation built
  - Media3 ExoPlayer player composable created to stream and preview fetched Instagram video URLs
  - MainViewModel manages state transitions for fetching, previewing, and initiating download
- **Duration:** 3m 10s

### Task_3_BuildDownloadsGalleryScreen: Implement Saved Downloads Gallery screen with Room database backing, video playback modal/player, share/delete actions, and navigation structure.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Gallery tab displays saved downloaded Instagram videos from Room DB
  - In-app playback, share video intent, and delete media functionality built
  - Navigation seamlessly switches between Saver main screen and Gallery screen
- **StartTime:** 2026-10-08 15:54:55 CEST

### Task_4_AppIconAndThemePolish: Create adaptive app icon with Instagram gradient aesthetic, add glassmorphic UI polish, and adjust visual components.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Adaptive app icon added matching app functionality and Instagram color theme
  - UI polish applied with clean M3 cards, glassmorphic effects, and edge-to-edge layout

### Task_5_RunAndVerify: Build and verify application stability, verify all tests pass, confirm zero crashes, and ensure all user requirements are met.
- **Status:** PENDING
- **Acceptance Criteria:**
  - build pass
  - app does not crash
  - make sure all existing tests pass
  - All Instagram downloader features (fetch, preview, download, gallery) work smoothly without crashes

