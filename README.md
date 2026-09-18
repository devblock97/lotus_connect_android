# Lotus Connect Android

Lotus Connect is an Android application built with Jetpack Compose, Kotlin, Material 3, and Retrofit.

---

## 🛠️ Tech Stack & Requirements

- **Language:** Kotlin 2.2.10
- **UI Framework:** Jetpack Compose (BOM 2026.02.01) with Material 3
- **Build Tool:** Gradle 9.7.1
- **Android Gradle Plugin (AGP):** 9.2.1
- **JDK:** Java 21 (Temurin / OpenJDK)
- **Min SDK:** 24 | **Target & Compile SDK:** 36

---

## 🚀 Local Development

### Build & Run
```bash
# Compile and build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Run Android Lint
./gradlew lintDebug

# Build release APK and App Bundle
./gradlew assembleRelease bundleRelease
```

---

## 🔄 CI/CD Pipelines (GitHub Actions)

This repository includes automated Continuous Integration and Continuous Delivery workflows located in `.github/workflows/`.

### 1. Continuous Integration (`ci.yml`)
- **Triggers:**
  - Pushes to `main`, `develop`, and `feature/*`, `bugfix/*`, `configuration/*` branches.
  - Pull Requests targeting `main` and `develop`.
  - Manual triggers (`workflow_dispatch`).
- **Jobs:**
  - **Lint & Code Quality:** Executes `./gradlew lintDebug` and uploads HTML/XML lint reports.
  - **Unit Tests:** Executes `./gradlew testDebugUnitTest` and uploads test result reports.
  - **Build Debug APK:** Executes `./gradlew assembleDebug` upon passing tests/lint and attaches the debug APK as an artifact for testing.

### 2. Continuous Delivery & Releases (`cd.yml`)
- **Triggers:**
  - Creation of version tags (e.g., `git tag v1.0.0 && git push origin v1.0.0`).
  - Manual trigger via GitHub Actions UI with custom release notes, draft, and pre-release options.
- **Actions:**
  - Injects release keystore credentials (if configured).
  - Builds the release APK (`assembleRelease`) and Google Play App Bundle (`bundleRelease`).
  - Creates a GitHub Release and attaches the generated `.apk` and `.aab` bundles.

---

## 🔐 Configuring Production App Signing Secrets

To enable automatic APK and AAB signing in the CD workflow:

1. Convert your keystore file to Base64:
   ```bash
   # macOS / Linux
   base64 -i my-release-key.jks | tr -d '\n'
   ```
2. Navigate to your GitHub repository: **Settings > Secrets and variables > Actions > New repository secret**.
3. Add the following repository secrets:
   - `KEYSTORE_BASE64`: The Base64-encoded string of your keystore file.
   - `KEYSTORE_PASSWORD`: The password for your keystore.
   - `KEY_ALIAS`: The alias for your release key.
   - `KEY_PASSWORD`: The private key password.

> **Note:** If these secrets are not configured, the CD workflow will still build release artifacts (as unsigned binaries) without breaking.