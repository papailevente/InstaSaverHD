# Release Notes - InstaSaver HD v1.1.0

## 🚀 What's New in Version 1.1.0

### 🌟 New Features & Enhancements
- **Product Variants (`standard` & `pro`)**: Added Gradle product flavors supporting both `standard` and `pro` tiers with custom Application IDs (`com.example.instasaverhd.pro`) and distinct UI branding badges.
- **Dynamic Pro Branding**: Added variant awareness in `HomeScreen` to highlight Pro status for premium builds.
- **Gradle & Toolchain Sync**: Upgraded Android Gradle Plugin to **9.4.1**, Google DevTools KSP to **2.3.6**, and Gradle distribution wrapper to **9.6.0**.
- **Comprehensive Documentation**: Added official project `README.md` with build instructions, architecture details, product variant breakdown, and testing guide.

### 🐛 Improvements & Fixes
- Enhanced URL parsing and sanitization for Instagram Reel and Post links.
- Verified test suite with 100% pass rate across all build variants (`testStandardDebugUnitTest` and `testProDebugUnitTest`).
- Optimized `ExoPlayer` resource management and lifecycle cleanup.

---

## 🛠 Build Verification Summary
- **Unit Tests**: 22 passed across `standard` and `pro` test tasks.
- **Gradle Build**: Successfully assembled `StandardDebug`, `ProDebug`, and `ProRelease` variants.
