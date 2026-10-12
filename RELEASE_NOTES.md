# Release Notes - InstaSaver HD v1.5.0 (Version Code 6)

## 🚀 What's New in Version 1.5.0

### 📱 Unified Single App Release
- **Simplified Single Version**: Removed product flavors (`pro` / `standard`) to maintain a single unified InstaSaver HD app build.

### 🌐 Fixed JWT Auth Error (`error.api.auth.jwt.missing`)
- **JWT Error Failover**: Automatically detects instances requiring JWT authentication (`error.api.auth.jwt.missing`) and skips them to route through open public Cobalt v10 server instances (`api.cobalt.tools`, `cobalt.api.scraye.com`, `cobalt.qtf.rs`, `cobalt.stream`, `cobalt-api.kwiatekmom.pl`, `cobalt.xyx.moe`).
- **Sanitized Error Messaging**: Suppressed raw technical error codes (`error.api.auth.jwt.missing`) in favor of clear human-readable guidance.

### 📥 Download APK
- 📱 [Download InstaSaver HD APK (v1.5.0)](https://github.com/papailevente/InstaSaverHD/releases/download/v1.5.0/InstaSaverHD-v1.5.0.apk)

---

## 🛠 Build Verification Summary
- **Unit Tests**: 19 passed across unit test tasks.
- **Gradle Build**: Successfully assembled `Release` APK.
