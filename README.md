# InstaSaver HD 🚀

**InstaSaver HD** is a high-performance, modern Android application built with **Jetpack Compose** and **Material 3** for fetching, previewing, and downloading Instagram reels, videos, and media in high definition.

---

## 📥 Direct APK Downloads (v1.1.0)

Download the latest pre-compiled Android APKs directly from the repository:

- 📱 **[Download InstaSaver HD Standard APK v1.1.0](https://github.com/papailevente/InstaSaverHD/raw/main/releases/InstaSaverHD-v1.1.0-standard.apk)** (`com.example.instasaverhd`)
- ⚡ **[Download InstaSaver HD Pro APK v1.1.0](https://github.com/papailevente/InstaSaverHD/raw/main/releases/InstaSaverHD-v1.1.0-pro.apk)** (`com.example.instasaverhd.pro`)

---

## 🌟 Features

- **Single-Tap URL Fetching & Validation**: Paste Instagram URLs with auto-cleaning and instant format validation.
- **Cobalt API Integration**: Fast metadata and media stream retrieval without requiring login or user credentials.
- **Embedded ExoPlayer Preview**: Play HD video previews directly inside the home feed before downloading.
- **Full-Screen Media Viewer**: Full controls, playback speed toggles, and loop support via Media3 ExoPlayer.
- **Background Downloads**: Managed via Android `DownloadManager` with status updates and progress notifications.
- **Local Gallery Management**: Offline access to downloaded items stored in Room DB with share, delete, and filtering options.
- **Modern Glassmorphic UI**: Custom dark theme with frosted glass cards (`GlassCard`) and Instagram-inspired gradients.
- **Product Flavors**: Supports `standard` (free) and `pro` (HD Ultra Fast) build variants.

---

## 📱 Product Variants

InstaSaver HD supports two build flavors under the `tier` dimension:

| Variant | Application ID | Version Name | Download Link |
| :--- | :--- | :--- | :--- |
| **Standard** | `com.example.instasaverhd` | `1.1.0-standard` | [InstaSaverHD-v1.1.0-standard.apk](https://github.com/papailevente/InstaSaverHD/raw/main/releases/InstaSaverHD-v1.1.0-standard.apk) |
| **Pro HD** | `com.example.instasaverhd.pro` | `1.1.0-pro` | [InstaSaverHD-v1.1.0-pro.apk](https://github.com/papailevente/InstaSaverHD/raw/main/releases/InstaSaverHD-v1.1.0-pro.apk) |

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

# Run unit tests across all variants
./gradlew test

# Build Standard Release APK
./gradlew assembleStandardRelease

# Build Pro Release APK
./gradlew assembleProRelease
```

---

## 🧪 Testing

The repository includes unit tests covering data models, API responses, URL parsing, and ViewModels:

- `MainViewModelTest`: Verifies state flow transitions, URL validation, and download actions.
- `CobaltModelsTest`: Validates Moshi serialization/deserialization for Cobalt API models.
- `InstagramUrlParserTest`: Tests url cleaning and validation.

Run all unit tests:
```bash
./gradlew test
```

---

## 📜 License & Legal Disclaimer

This project is intended for educational and personal use only. Users are responsible for complying with Instagram's terms of service and content distribution rights.
