# Release Notes - InstaSaver HD v1.4.0 (Version Code 5)

## 🚀 What's New in Version 1.4.0

### 🌐 Fixed Server Fallbacks & DNS Exceptions
- **Removed Shut-Down Domains**: Completely removed `co.wuk.sh` from server fallback list to eliminate `Unable to resolve host` DNS exception popups.
- **Updated Active Cobalt Instances**: Added active Cobalt v10 server list (`api.cobalt.tools`, `cobalt.api.scraye.com`, `cobalt.qtf.rs`, `cobalt.stream`).
- **Clean Error Formatting**: Improved exception handling to ensure raw DNS resolution errors are hidden and user-friendly error guidance is displayed.
- **Optimized Request Payload**: Refined `CobaltRequest` payload structure for Cobalt v10 compatibility.

### 📥 Download APKs
- 📱 [Download Standard Variant APK (v1.4.0)](https://github.com/papailevente/InstaSaverHD/releases/download/v1.4.0/InstaSaverHD-v1.4.0-standard.apk)
- ⚡ [Download Pro Variant APK (v1.4.0)](https://github.com/papailevente/InstaSaverHD/releases/download/v1.4.0/InstaSaverHD-v1.4.0-pro.apk)

---

## 🛠 Build Verification Summary
- **Unit Tests**: 38 passed across `standard` and `pro` test tasks.
- **Gradle Build**: Successfully assembled `StandardRelease` and `ProRelease` variants.
