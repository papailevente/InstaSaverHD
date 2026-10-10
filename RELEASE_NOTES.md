# Release Notes - InstaSaver HD v1.1.0 (Version Code 2)

## 🚀 What's New in Version 1.1.0

### 📥 Download APKs
- 📱 [Download Standard Variant APK (v1.1.0)](https://github.com/papailevente/InstaSaverHD/raw/main/releases/InstaSaverHD-v1.1.0-standard.apk)
- ⚡ [Download Pro Variant APK (v1.1.0)](https://github.com/papailevente/InstaSaverHD/raw/main/releases/InstaSaverHD-v1.1.0-pro.apk)

---

### 🌟 New Features & Enhancements
- **Cobalt v10 API Endpoint & Failover**: Migrated API requests to Cobalt v10 (`/`) and added multi-instance fallback support (`api.cobalt.tools`, `cobalt.api.scraye.com`, `co.wuk.sh`).
- **Product Variants (`standard` & `pro`)**: Added Gradle product flavors supporting both `standard` and `pro` tiers with custom Application IDs (`com.example.instasaverhd.pro`) and distinct UI branding badges.
- **Dynamic Pro Branding**: Added variant awareness in `HomeScreen` to highlight Pro status for premium builds.
- **Gradle & Toolchain Sync**: Upgraded Android Gradle Plugin to **9.4.1**, Google DevTools KSP to **2.3.6**, and Gradle distribution wrapper to **9.6.0**.
- **Tracked Release APKs**: Pre-compiled, signed APK binaries published directly in the repository under `/releases`.

---

## 🛠 Build Verification Summary
- **Unit Tests**: 24 passed across `standard` and `pro` test tasks.
- **Gradle Build**: Successfully assembled `StandardRelease` and `ProRelease` variants.
