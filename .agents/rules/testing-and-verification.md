---
trigger: glob
globs: "**/domain/**/*.kt, **/data/**/*.kt, **/presentation/**/*.kt, **/*Test.kt"
description: "Mandatory business-driven unit testing standards, correctness verification over shallow coverage, fake test doubles, and dead-code hygiene."
---

# Business-Driven Unit Testing & Correctness Verification Rules

## 1. Mandatory Unit Tests for Every Feature
- Every new or modified **domain entity/use case** (`:domain`), **repository/mapper** (`:data`), and **Model-View-Intent `ViewModel`** (`:composeApp`) **must** include unit tests in the same change.
- **Mechanical Gate:** `:composeApp:verifyUiArchitecture` verifies that every `*ViewModel.kt` in `presentation/` has a matching `*ViewModelTest.kt` under `src/commonTest/`.

## 2. Correctness Verification Over Coverage Theater
Write tests that prove **business behavior and invariants**, not line-count metrics:
1. **Business Scenarios & Boundaries:** Name test functions after the domain behavior being verified (`routineAtExactFourExerciseBoundaryDisplaysAllExercisesWithZeroHiddenCount`, `reselectingActiveDestinationSuppressesDuplicateNavigationEffect`). Always cover boundary conditions (`0`, `exact limit`, `limit + 1`), initialization from persisted state, and error/fallback paths.
2. **Complete State + Side-Effect Verification:**
   - Assert meaningful `State` transitions before and after `onIntent(...)`.
   - Assert exact emitted `Effect`s **and** verify the *absence* of unwanted side effects on idempotent or invalid actions (e.g., tapping the already-selected tab must not emit a duplicate navigation `Effect`).
3. **No Tautological Tests:** Never write brittle tests that merely re-assert hardcoded theme color literals, copy-paste enum values, or test framework getters without domain logic.

## 3. Clean Test Architecture & Zero Dead Code
- **One Focused Test Suite Per Subject:** Mirror the production package structure (`domain/model/RoutineOverviewTest.kt`, `presentation/shell/ApplicationShellViewModelTest.kt`)—never dump unrelated layers into a catch-all test class.
- **Reusable Stateful Fakes Over Heavy Mocking:** Implement `:domain` interfaces with concise, stateful fakes (`FakeAudioCueService`) in `commonTest` so tests verify observable behavior cleanly across all Kotlin Multiplatform targets.
- **Zero Dead Properties or Unused Helpers:** Keep enums, models, and `ViewModel`s free of unused constructor dependencies, dead fields, or redundant mapping functions (e.g., bind localized `StringResource` properties directly on UI enums like `TopLevelDestination` rather than keeping unused raw `String` labels alongside external `when` mappers).
