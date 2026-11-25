---
trigger: glob
globs: "**/domain/**/*.kt, **/data/**/*.kt, **/presentation/**/*.kt, **/dependencyinjection/**/*.kt"
description: "Multi-module Clean Architecture boundaries (:domain, :data, :composeApp), deep domain immutability, and official Model-View-Intent Unidirectional Data Flow rules."
---

# Multi-Module Architecture & Model-View-Intent State Rules

## 1. Module Dependency & Responsibility Boundaries
- **`:domain` (Pure Kotlin Core):**
  - Zero Compose, Android, UIKit, Room, DataStore, or Koin framework dependencies.
  - Owns rich business models (`domain.model`), repository interfaces (`domain.repository`), platform service contracts (`domain.service`), and focused use cases (`domain.usecase`).
  - **Deep Immutability Invariant:** Because [compose_stability.conf](../../compose_stability.conf) registers `com.jhonnyb.kindrep.domain.model.*` as stable for the Compose Compiler, every class in `domain/model/` **must** use `val` properties exclusively and read-only `List`/`Set`/`Map` interfaces—never `var` or `MutableCollection` (`:domain:verifyDomainImmutability` fails the build otherwise).
- **`:data` (Repository & Persistence Implementations):**
  - Depends on `:domain`. Implements `:domain` repository interfaces using local data sources (Room, DataStore Preferences, bundled JSON catalogs).
  - Maps persistence DTOs/entities to `:domain` models inside `:data`—never leak database entities or DataStore keys outside `:data`.
  - Emit distinct values from repository `Flow`s (`distinctUntilChanged()`) to avoid redundant downstream recompositions.
- **`:composeApp` (Presentation, User Interface, Platform & Dependency Injection):**
  - Depends on `:domain` and `:data`. Hosts Model-View-Intent `ViewModel`s (`presentation/`), Compose UI (`ui/`), `navigation/`, `platform/` (`expect`/`actual` implementations of `:domain` service contracts and UI effects), and Koin graph wiring (`dependencyinjection/`).

## 2. Official Model-View-Intent (Unidirectional Data Flow) Pattern
Follow Google's official Unidirectional Data Flow guidance formalized as **State + Intent + Effect**:

1. **Contract File (`<Feature>Contract.kt`):**
   - **`@Immutable data class <Feature>State(...)`:** Single immutable snapshot of everything needed to render the screen. Default values should represent a sensible initial state. Prefer pre-computed display properties over heavy formatting inside `@Composable` bodies.
   - **`sealed interface <Feature>Intent`:** Represents every user interaction or lifecycle event entering the `ViewModel` (e.g., `AcceptDisclaimer`, `SelectDestination`, `StartSession`).
   - **`sealed interface <Feature>Effect`:** Represents transient, one-off side effects (navigation triggers, audio chimes, snackbars) that must **not** replay on configuration change or recomposition.
2. **State Holder (`<Feature>ViewModel.kt`):**
   - Expose `val state: StateFlow<<Feature>State>` via `MutableStateFlow.asStateFlow()` (or `stateIn` over domain `Flow`s), updated atomically via `mutableState.update { it.copy(...) }`.
   - Expose `val effects: Flow<<Feature>Effect>` backed by `Channel<<Feature>Effect>(Channel.BUFFERED).receiveAsFlow()`.
   - Expose **one** public action entry point: `fun onIntent(intent: <Feature>Intent)`. Do not add ad-hoc public imperative mutation methods on `ViewModel`s (`:composeApp:verifyUiArchitecture` enforces `fun onIntent(` on every `ViewModel`).
3. **Composable Binding:**
   - Collect state at the route boundary with `collectAsStateWithLifecycle()`.
   - Collect `effects` inside `LaunchedEffect(viewModel)` at the route/navigation boundary.
   - Pass immutable `state` values and `onIntent: (<Feature>Intent) -> Unit` (or focused action lambdas) down to stateless screen composables so screens remain trivially previewable and testable without Koin or a `ViewModel`.
