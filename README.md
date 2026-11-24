# Kindrep

**Kindrep** is a calm, local-first, cross-platform (**Android & iOS**) guided recovery exercise player for any practitioner-prescribed plan (physiotherapy, chiropractic, osteopathy, athletic therapy, sports rehab, kinesiology) — built with **Kotlin Multiplatform** and **Compose Multiplatform**.

> **Important:** Kindrep is a habit & guided-playback companion for plans prescribed by a licensed healthcare practitioner. It does **not** diagnose injuries or prescribe medical exercises.

---

## Tech Stack

- **Shared UI:** Compose Multiplatform (`1.12.2`) + Material 3 (`Warm Earth` Light & `Soft Stone D3` Dark themes)
- **Language & Build:** Kotlin `2.3.20`, Android Gradle Plugin `9.5.0`, Gradle `9.7.1`
- **Typography & Shapes:** Bundled **Nunito** (`Regular`, `Medium`, `SemiBold`, `Bold` with `tnum` tabular numerals) & tuned `18 / 14 / 12 / 9dp` radii
- **Dependency Injection:** **Koin** (`4.2.2`) + **Koin Compiler Plugin** (`io.insert-koin.compiler.plugin`)
- **Navigation & State:** Type-safe `navigation-compose` (`2.10.0`), AndroidX `lifecycle-viewmodel-compose` (`2.11.0`), Kotlin Coroutines & `StateFlow`
- **Platform Bridges (`expect` / `actual`):**
  1. `LoopingExerciseVideo` — Seamless muted exercise loops (`Media3` / `AVPlayerLooper`)
  2. `AudioCuePlayer` — Non-intrusive audio cues with music ducking
  3. `KeepScreenAwake` — Active session wake lock
  4. `MediaResolver` — Local media & calm neutral-gray figure fallback
  5. `LockScreenOrientation` — Full-screen Landscape lock for floor-mat sessions (`04A–04E`)

## Project Structure

- `composeApp/` — Shared Kotlin Multiplatform & Compose Multiplatform UI, theme, mascots, DI, navigation, and `expect`/`actual` bridges (`commonMain`, `androidMain`, `iosMain`, `commonTest`)
- `androidApp/` — Android application entry point (`com.jhonnybarrios.kindrep`) & adaptive 2B launcher icons
- `iosApp/` — Xcode project wrapping the shared `ComposeApp` framework for iOS

## Development

```bash
# Run shared unit tests + build debug Android APK
./gradlew :composeApp:testAndroidHostTest :androidApp:assembleDebug

# Build iOS Simulator framework (macOS)
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```
