# InstaSaver HD 🚀

**InstaSaver HD** is a high-performance, modern Android application built with **Jetpack Compose** and **Material 3** for fetching, previewing, and downloading Instagram reels, videos, and media in high definition.

---

## 📥 Direct APK Download (v1.6.0)

Download the official single Android APK directly from the repository:

- 📱 **[Download InstaSaver HD APK v1.6.0](https://github.com/papailevente/InstaSaverHD/releases/download/v1.6.0/InstaSaverHD-v1.6.0.apk)** (`com.example.instasaverhd`)

---

## 🌟 Features

- **In-App Auto-Update & Real-Time Progress Bar**: Detects new GitHub releases on launch, streams update downloads with live progress percentages (0-100%), and opens the package installer automatically.
- **Open Public Cobalt Instances & JWT Bypass**: Bypasses private JWT auth restrictions by automatically routing through open public Cobalt v10 server instances (`api.cobalt.tools`, `cobalt.api.scraye.com`, `cobalt.qtf.rs`, `cobalt.stream`, `cobalt-api.kwiatekmom.pl`, `cobalt.xyx.moe`).
- **Clean User Guidance**: Sanitizes API response errors to hide technical codes (`error.api.auth.jwt.missing`) and show clear, human-readable guidance.
- **System "Share To" Integration**: Share Instagram links directly to InstaSaver HD from Signal, WhatsApp, Instagram, or Chrome.
- **Single-Tap URL Fetching & Validation**: Auto-cleans Instagram links and strips surrounding message text automatically.
- **Embedded ExoPlayer Preview**: Play HD video previews directly inside the home feed before downloading.
- **Full-Screen Media Viewer**: Full controls, playback speed toggles, and loop support via Media3 ExoPlayer.
- **Background Downloads**: Managed via Android `DownloadManager` with status updates and progress notifications.
- **Local Gallery Management**: Offline access to downloaded items stored in Room DB with share, delete, and filtering options.
- **Modern Glassmorphic UI**: Custom dark theme with frosted glass cards (`GlassCard`) and Instagram-inspired gradients.

---

## 🏗 Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 Design & Material Icons
- **Architecture**: MVVM + Clean Architecture with Unidirectional Data Flow (`StateFlow` / `UiState`)
- **Dependency Injection & Async**: Kotlin Coroutines, `StateFlow`, `collectAsStateWithLifecycle`
- **Database**: [Room Database](https://developer.android.com/training/data-storage/room) with KSP compiler and Flow observers
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) + [Moshi](https://github.com/square/moshi) JSON parser + OkHttp 3
- **Media Playback**: [Jetpack Media3 ExoPlayer](https://developer.android.com/guide/topics/media/media3)
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Build System**: Gradle 9.6 + AGP 9.4 + KSP 2.3 + Java 21

---

## ⚙️ Building & Running

### Prerequisites
- Android Studio Ladybug / ME or newer
- JDK 21
- Android SDK 37 (Min SDK 35, Target SDK 36)

### Build Commands

```bash
# Clone the repository
git clone https://github.com/papailevente/InstaSaverHD.git
cd InstaSaverHD

# Run unit tests
./gradlew test

# Build Release APK
./gradlew assembleRelease
```

---

## 🧪 Testing

The repository includes unit tests covering data models, API responses, URL parsing, ViewModels, and update managers:

- `UpdateManagerTest`: Validates version comparison logic and update triggers.
- `MainViewModelTest`: Verifies state flow transitions, URL validation, and download actions.
- `CobaltModelsTest`: Validates Moshi serialization/deserialization for Cobalt API models.
- `InstagramUrlParserTest`: Tests url cleaning and embedded link extraction.

Run all unit tests:
```bash
./gradlew test
```

---

## 📜 License & Legal Disclaimer

This project is intended for educational and personal use only. Users are responsible for complying with Instagram's terms of service and content distribution rights.
