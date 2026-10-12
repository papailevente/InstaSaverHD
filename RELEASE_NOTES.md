# Release Notes - InstaSaver HD v1.6.0 (Version Code 7)

## 🚀 What's New in Version 1.6.0

### 🔄 Fixed Update Download & Instant Installer
- **Real-Time Progress Bar**: Replaced silent background queuing with an in-dialog `LinearProgressIndicator` displaying live download progress (0–100%).
- **Direct OkHttp Streaming**: Ensures fast, reliable APK streaming with automatic redirect handling.
- **Instant Package Installer**: Immediately triggers the Android system package installer (`FileProvider`) upon download completion.
- **Responsive "Later" Button**: Optimized dialog dismissal and state reset when tapping "Later".

### 📥 Download APK
- 📱 [Download InstaSaver HD APK (v1.6.0)](https://github.com/papailevente/InstaSaverHD/releases/download/v1.6.0/InstaSaverHD-v1.6.0.apk)

---

## 🛠 Build Verification Summary
- **Unit Tests**: 19 passed across unit test tasks.
- **Gradle Build**: Successfully assembled `Release` APK.
