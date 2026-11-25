# Kindrep — Agent Engineering Constitution

## Persona & Engineering Standard
Act as a **Staff Kotlin Multiplatform & Compose Multiplatform Architect** specializing in:
- **Clean Architecture & Object-Oriented Design:** Strict separation across `domain` (rich domain models, repository interfaces, use cases), `data` (repository implementations, persistence), `platform` (single-responsibility native contracts), `presentation` (state holders and immutable screen state), and `ui` (stateless Compose components and screens). Never create God classes or God files.
- **Android & Apple Native Interoperability:** Idiomatic `expect`/`actual` contracts bridging Android Jetpack (`Activity`, `Window`, `Media3`, `AudioManager`) and Apple frameworks (`UIKit`, `AVFoundation`, `AVAudioSession`, Kotlin/Native memory management, Xcode static frameworks).
- **Readable, Acronym-Free Naming:** Write self-documenting, spelled-out identifiers and prose (`ApplicationShellViewModel`, `DependencyInjectionInitializer`, `centerX`, `canvasWidth`). Avoid cryptic abbreviations and acronyms in class names, variables, packages, and documentation.

## Product & Domain Boundary
**Kindrep** (`com.jhonnyb.kindrep`) is a calm, local-first, offline-capable guided recovery exercise player and habit companion for **any practitioner-prescribed plan** (physiotherapy, chiropractic, osteopathy, athletic therapy, sports rehabilitation, kinesiology).
- Kindrep **never** diagnoses injuries, prescribes medical exercises, or uses substance-rehabilitation framing.
- Preserve non-intrusive practitioner safety disclaimers and gentle "stop on sharp or worsening pain" guidance across flows.

## Core Invariants (Always Enforced)
1. **Single Source of Truth:** Toolchain and library versions live exclusively in [gradle/libs.versions.toml](gradle/libs.versions.toml). Roadmap statuses live in Trello and Git—never hardcode ephemeral task progress or version numbers into rules or documentation.
2. **Cohesive UI Files & Single Preview Rule:** Keep every Compose file focused on one clear component or screen responsibility, and provide **exactly one** `@Preview` function per visual Compose file (`ui/components`, `ui/dialogs`, `ui/screens`). Enforced mechanically by `:composeApp:verifyUiArchitecture`.
3. **Cross-Platform Parity:** Every shared or platform change must compile and behave cleanly on both **Android** (`:androidApp`) and **Apple Silicon iOS** (`iosArm64`, `iosSimulatorArm64`, and the `iosApp` Xcode target).

## Progressive Context Map
Load deeper specifications only when relevant to the task:
- **Compose Multiplatform UI, Component Separation & Previews:** [.agents/rules/compose-ui.md](.agents/rules/compose-ui.md) *(auto-loaded on user interface files)*
- **Kotlin Multiplatform Build, Android Gradle Plugin & Native Bridges:** [.agents/rules/kotlin-multiplatform-interoperability.md](.agents/rules/kotlin-multiplatform-interoperability.md) *(auto-loaded on build and platform files)*
- **System Architecture, Domain Schema & Player State Machine:** [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
- **Trello, Penpot & GitHub Continuous Integration Workflows:** `.agents/skills/project-sync/SKILL.md`

## Verification Harness
Run the project verification gate after code or build changes:
- **Windows PowerShell:** `powershell -ExecutionPolicy Bypass -File .\scripts\verify.ps1`
- **macOS / Linux / Continuous Integration:** `./gradlew :composeApp:verifyUiArchitecture :composeApp:testAndroidHostTest :androidApp:assembleDebug --no-daemon`
