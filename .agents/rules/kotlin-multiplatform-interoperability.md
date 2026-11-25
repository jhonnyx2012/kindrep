---
trigger: glob
globs: "**/build.gradle.kts, **/settings.gradle.kts, **/libs.versions.toml, **/compose_stability.conf, **/platform/**/*.kt, **/androidMain/**/*.kt, **/iosMain/**/*.kt, iosApp/**"
description: "Multi-module Kotlin Multiplatform Gradle setup, Compose compiler stability configuration, Apple Silicon iOS interop, and single-responsibility native bridges."
---

# Kotlin Multiplatform Build, Stability & Native Interoperability Rules

## 1. Multi-Module Gradle Setup (Android Gradle Plugin 9+)
- **`:domain`:** Pure `org.jetbrains.kotlin.multiplatform` module (`jvm()`, `iosArm64()`, `iosSimulatorArm64()`). Keep free of Android and Compose plugins so domain tests execute instantaneously on `:domain:jvmTest`.
- **`:data`:** Applies `org.jetbrains.kotlin.multiplatform` and `com.android.kotlin.multiplatform.library` (`namespace = "com.jhonnyb.kindrep.data"`), depending on `:domain`.
- **`:composeApp`:** Applies `org.jetbrains.kotlin.multiplatform`, `com.android.kotlin.multiplatform.library` (`namespace = "com.jhonnyb.kindrep.shared"`), `composeMultiplatform`, `composeCompiler`, and `koinCompiler`. Attaches preview rendering via `"androidRuntimeClasspath"(libs.compose.ui.tooling)`.
- **`:androidApp`:** Applies `com.android.application` with built-in Kotlin support—**never** apply `org.jetbrains.kotlin.android`.
- **Compose Cross-Module Stability:** Keep `composeCompiler { stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose_stability.conf")) }` configured on Compose-enabled modules so deeply-immutable `:domain` models are treated as stable across module boundaries.
- **Compiler Plugin & Apple Silicon Alignment:** Verify Kotlin compiler and iOS `.klib` ABI compatibility against [gradle/libs.versions.toml](../../gradle/libs.versions.toml) before adding compiler plugins (Room, Symbol Processing, Koin). Target `iosArm64()` and `iosSimulatorArm64()` only, keeping `EXCLUDED_ARCHS[sdk=iphonesimulator*]=x86_64` in [iosApp/Configuration/Config.xcconfig](../../iosApp/Configuration/Config.xcconfig).

## 2. Single-Responsibility Native Contracts (`expect` / `actual`)
- **Focused Files:** Give each native capability (`LoopingExerciseVideo`, `KeepScreenAwakeEffect`, `ScreenOrientationEffect`, `PlatformServices`) its own single-responsibility `expect`/`actual` file.
- **Domain Service Inversion:** Define non-visual platform contracts (`AudioCueService`, `MediaAssetLocator`) in `:domain` and bind platform implementations via Koin in `:composeApp` so ViewModels remain pure and unit-testable.
- **Lifecycle & Audio Etiquette:** Release native players, wake locks, and orientation locks inside `DisposableEffect.onDispose`. Always configure transient audio ducking (`AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK` on Android; `.playback` with `[.mixWithOthers, .duckOthers]` on iOS) so background music is never interrupted.
