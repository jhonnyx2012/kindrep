# Kindrep

**Kindrep** (`com.jhonnyb.kindrep`) is a calm, local-first, cross-platform (**Android & iOS**) guided recovery exercise player for any practitioner-prescribed plan (physiotherapy, chiropractic, osteopathy, athletic therapy, sports rehabilitation, kinesiology) — built with **Kotlin Multiplatform** and **Compose Multiplatform**.

> **Important:** Kindrep is a habit and guided-playback companion for plans prescribed by a licensed healthcare practitioner. It does **not** diagnose injuries or prescribe medical exercises.

---

## Architecture & Engineering Principles

*(Library and plugin versions live exclusively in [gradle/libs.versions.toml](gradle/libs.versions.toml); full domain models and state machines live in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).)*

- **Clean Architecture & Object-Oriented Separation:** Clear boundaries across `domain`, `data`, `platform`, `presentation`, `navigation`, `dependencyinjection`, and `ui` (`theme`, `components`, `dialogs`, `screens`).
- **Focused User Interface Files:** Single-responsibility Compose components and screens with **one** `@Preview` per visual file, enforced automatically by `:composeApp:verifyUiArchitecture`.
- **Design System:** Material 3 (`Warm Earth` Light & `Soft Stone D3` Dark themes), bundled **Nunito** typography with tabular numerals (`tnum`), and tuned `18 / 14 / 12 / 9.dp` corner radii.
- **Native Platform Contracts (`expect` / `actual`):**
  1. `LoopingExerciseVideo` — Seamless muted exercise loops (`Media3` / `AVPlayerLooper`)
  2. `AudioCueService` — Non-intrusive audio cues with transient music ducking
  3. `KeepScreenAwakeEffect` — Active session wake lock
  4. `MediaAssetLocator` — Local media lookup with calm neutral-gray figure fallback
  5. `ScreenOrientationEffect` — Full-screen Landscape lock for floor-mat sessions

## Project Structure

- `composeApp/` — Shared Kotlin Multiplatform domain, data, presentation, navigation, dependency injection, platform contracts, and Compose Multiplatform user interface (`commonMain`, `androidMain`, `iosMain`, `commonTest`)
- `androidApp/` — Android application entry point (`com.jhonnyb.kindrep`) and adaptive launcher icons
- `iosApp/` — Xcode project wrapping the shared `ComposeApp` framework for iOS
- `AGENTS.md` & `.agents/` — Agent engineering constitution, scoped rules, and project synchronization skills
- `docs/ARCHITECTURE.md` — Product boundaries, Clean Architecture layers, persistence schema, and player state machine

## Development

```powershell
# Windows verification harness (UI architecture & preview gate + host unit tests + debug Android build)
powershell -ExecutionPolicy Bypass -File .\scripts\verify.ps1
```

```bash
# macOS / Linux verification + iOS Simulator framework
./gradlew :composeApp:verifyUiArchitecture :composeApp:testAndroidHostTest :androidApp:assembleDebug
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```
