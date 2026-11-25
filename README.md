# Kindrep

**Kindrep** (`com.jhonnyb.kindrep`) is a calm, local-first, cross-platform (**Android & iOS**) guided recovery exercise player for any practitioner-prescribed plan (physiotherapy, chiropractic, osteopathy, athletic therapy, sports rehabilitation, kinesiology) — built with **Kotlin Multiplatform** and **Compose Multiplatform**.

> **Important:** Kindrep is a habit and guided-playback companion for plans prescribed by a licensed healthcare practitioner. It does **not** diagnose injuries or prescribe medical exercises.

---

## Architecture & Engineering Principles

*(Library and plugin versions live exclusively in [gradle/libs.versions.toml](gradle/libs.versions.toml); full domain models and state machines live in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).)*

- **Multi-Module Clean Architecture:** Physical Gradle module separation across `:domain` (pure Kotlin deeply-immutable models and contracts), `:data` (repositories and local persistence), `:composeApp` (Model-View-Intent presentation, decomposed Compose UI, navigation, platform bridges, and dependency injection), `:androidApp`, and `iosApp/`.
- **Model-View-Intent (Unidirectional Data Flow):** Immutable `@Immutable` screen states, sealed `Intent` actions routed through a single `onIntent(...)` handler, and buffered `Effect` streams for one-off events.
- **Recomposition-Safe, Decomposed Compose UI:** Outline-clear screens built from small, legible sub-composables, localized `strings.xml` resources, cross-module stability via `compose_stability.conf`, phase-deferred animation/timer reads, and **one** `@Preview` per visual file.

## Project Structure

- `domain/` — Pure Kotlin Multiplatform domain models, repository interfaces, use cases, and service contracts
- `data/` — Kotlin Multiplatform repository implementations and local persistence
- `composeApp/` — Shared Compose Multiplatform UI, Model-View-Intent ViewModels, navigation, Koin dependency injection, and `expect`/`actual` bridges
- `androidApp/` — Android application entry point (`com.jhonnyb.kindrep`) and adaptive launcher icons
- `iosApp/` — Xcode project wrapping the shared `ComposeApp` framework for iOS
- `AGENTS.md` & `.agents/` — Everlasting agent engineering constitution, scoped rules, and project synchronization skills

## Development

```powershell
# Windows multi-module verification harness
powershell -ExecutionPolicy Bypass -File .\scripts\verify.ps1
```

```bash
# macOS / Linux verification + iOS Simulator framework
./gradlew :domain:verifyDomainImmutability :domain:jvmTest :data:testAndroidHostTest :composeApp:verifyUiArchitecture :composeApp:testAndroidHostTest :androidApp:assembleDebug
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```
