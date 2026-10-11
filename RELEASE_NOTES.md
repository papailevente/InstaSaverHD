# Release Notes - InstaSaver HD v1.3.0 (Version Code 4)

## 🚀 What's New in Version 1.3.0

### 🔄 Auto-Update Checker & In-App Installer
- **Automatic Startup Check**: On launch, the app automatically checks the GitHub Releases API for new updates.
- **Update Dialog**: Displays a frosted glass dialog showing release notes, version tag, and options to **"Update Now"** or **"Later"**.
- **In-App APK Installer**: Downloads matching variant update APK (`standard` or `pro`) via `DownloadManager` and prompts instant installation via Android `FileProvider`.

### 📲 System "Share To" Menu Integration
- **Direct Share Support**: Share Instagram links directly to InstaSaver HD from Signal, WhatsApp, Instagram, or Chrome.

### 📥 Download APKs
- 📱 [Download Standard Variant APK (v1.3.0)](https://github.com/papailevente/InstaSaverHD/releases/download/v1.3.0/InstaSaverHD-v1.3.0-standard.apk)
- ⚡ [Download Pro Variant APK (v1.3.0)](https://github.com/papailevente/InstaSaverHD/releases/download/v1.3.0/InstaSaverHD-v1.3.0-pro.apk)

---

## 🛠 Build Verification Summary
- **Unit Tests**: 38 passed across `standard` and `pro` test tasks.
- **Gradle Build**: Successfully assembled `StandardRelease` and `ProRelease` variants.
