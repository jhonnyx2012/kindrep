---
trigger: glob
globs: "**/build.gradle.kts, **/libs.versions.toml, **/platform/**/*.kt, **/androidMain/**/*.kt, **/iosMain/**/*.kt, iosApp/**"
description: "Kotlin Multiplatform build configuration, Android Gradle Plugin 9+ module separation, Apple Silicon iOS interop, and single-responsibility platform contracts."
---

# Kotlin Multiplatform Build & Native Interoperability Rules

## 1. Module & Build Architecture (Android Gradle Plugin 9+)
- **Application vs. Shared Library Separation:**
  - `:androidApp` applies `com.android.application` with built-in Kotlin support—**never** apply `org.jetbrains.kotlin.android`.
  - `:composeApp` applies `com.android.kotlin.multiplatform.library` using the `kotlin { android { ... } }` block (`namespace = "com.jhonnyb.kindrep.shared"`).
- **Compose Preview Tooling in Multiplatform:** Attach the Android preview renderer via root `dependencies { "androidRuntimeClasspath"(libs.compose.ui.tooling) }` and keep `libs.compose.ui.tooling.preview` in `commonMain.dependencies`.
- **Compiler Plugin & Binary Interface Alignment:** Before upgrading Kotlin or adding compiler plugins (Room, Symbol Processing, Koin Compiler Plugin, Compose), verify exact Kotlin compiler version and Apple `.klib` binary compatibility against [gradle/libs.versions.toml](../../gradle/libs.versions.toml).
- **Apple Silicon Targets:** Target `iosArm64()` and `iosSimulatorArm64()` only (`iosX64` is retired). Keep `EXCLUDED_ARCHS[sdk=iphonesimulator*]=x86_64` in [iosApp/Configuration/Config.xcconfig](../../iosApp/Configuration/Config.xcconfig).

## 2. Single-Responsibility Native Contracts (`expect` / `actual`)
- **One Responsibility Per Bridge File:** Never group unrelated platform capabilities into a monolithic bridge file. Give each native capability (`LoopingExerciseVideo`, `KeepScreenAwakeEffect`, `ScreenOrientationEffect`, `PlatformAudioCueService`, `PlatformMediaAssetLocator`) its own focused `expect` declaration and per-platform `actual` implementation.
- **Domain Inversion:** Expose platform services (`AudioCueService`, `MediaAssetLocator`) as clean interfaces in `domain/` and wire platform implementations through `dependencyinjection/` so `presentation/` and `domain/` remain 100% platform-agnostic and unit-testable with plain Kotlin doubles.
- **Lifecycle Safety & Audio Ducking:**
  - Bind native players (`ExoPlayer` / `AVQueuePlayer` + `AVPlayerLooper`), audio sessions, orientation locks, and wake locks to `DisposableEffect` or structured coroutine scopes so resources always release on disposal or backgrounding.
  - Audio chimes and voice cues must duck transiently over background music (`AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK` on Android; `.playback` with `[.mixWithOthers, .duckOthers]` on iOS)—never interrupt user music playback.
