---
trigger: glob
globs: "**/ui/**/*.kt, **/navigation/**/*.kt, **/*Root.kt"
description: "Compose Multiplatform Clean Architecture separation, single @Preview per file rule, acronym-free naming, and Kindrep design system invariants."
---

# Compose Multiplatform User Interface & Design System Rules

## 1. Clean Component Separation & Single `@Preview` Rule
- **Single Responsibility Per File:** Never combine multiple screens, unrelated cards, or dialogs into a single file. Organize user interface code into focused packages:
  - `ui/theme/` — Color palettes, typography, shapes, and theme provider (split cleanly by concern).
  - `ui/components/` — Reusable, cohesive widgets (`RoutineSummaryCard`, `CompanionStreakCard`, `KindrepBottomNavigationBar`, `KindrepBunnyMascot`). Private helper composables or dedicated canvas renderer classes belong in the same file **only** when they serve that single component, keeping at most 4 `@Composable` functions per file.
  - `ui/dialogs/` — Standalone modal dialogs (`SafetyNoticeDialog`).
  - `ui/screens/<feature>/` — Stateless screen composables taking immutable state and event lambdas.
  - `navigation/` — Route definitions and `KindrepNavigationHost` wiring state holders to stateless screens.
- **Exactly One `@Preview` Per Visual File:** Every file under `ui/components/`, `ui/dialogs/`, and `ui/screens/` must declare **exactly one** `@Preview` function (`androidx.compose.ui.tooling.preview.Preview`) wrapped in `KindrepTheme`. Do not duplicate Light/Dark preview functions in the same file; rely on IDE preview mode toggles when inspecting alternate themes.
- **Preview Safety:** Never instantiate native video players, sensors, or `Activity` references unconditionally inside previewed composables; guard native views with `LocalInspectionMode.current` and render `ExerciseFigurePlaceholder` during inspection.
- **Mechanical Enforcement:** `:composeApp:verifyUiArchitecture` fails the build if a visual user interface file has zero or multiple `@Preview` functions, exceeds 4 `@Composable` functions, or grows past 700 lines.

## 2. Localization & Constants
- **No Hardcoded User-Facing Text:** Every visible string in `ui/` and `navigation/` must be declared in `composeResources/values/strings.xml` and resolved via `stringResource(Res.string.*)`.
- **Reusable Keys & Constants:** Extract repeatable identifiers, audio cue keys, or route/test constants into dedicated constant objects (only when actually reused).

## 2. Expressive, Acronym-Free Code Style
- Use complete, descriptive domain and geometry names (`centerX`, `centerY`, `canvasWidth`, `canvasHeight`, `viewModel`, `routineIdentifier`) rather than cryptic abbreviations (`cx`, `cy`, `w`, `h`, `vm`, `id` shorthand where ambiguous).
- Encapsulate complex `Canvas` drawing or formatting logic in cohesive helper classes or methods with clear responsibilities.

## 3. Locked Visual & Copy Invariants
- **Theme Tokens (`KindrepPalette.kt`):** Read semantic colors exclusively via `LocalKindrepPalette.current` (`WarmEarthPalette` for Light / `SoftStoneD3Palette` for Dark) and shapes/typography via `MaterialTheme`.
- **Typography & Floor-Mat Legibility:** Bundled `Nunito` with tabular numerals (`fontFeatureSettings = "tnum"`). Routine card titles use `Bold` (`20–21.sp`), while exercise row titles inside cards use `Regular` (`16.sp`). Maintain `14.sp+` minimum labels and `56.dp+` primary touch targets.
- **Corner Radii:** Modals and sheets `18.dp`, cards / primary buttons / active navigation pill `14.dp`, inner rows and inputs `12.dp`, badges and chips `9.dp`.
- **Exercise Value Copy:** Display **real numeric values only** (`10 reps`, `8 / side`, `12 reps · 2 sets`, `45s hold`). Never display `"default"` or `"Edited"` badges.
- **Navigation & Routine Cards:**
  - `KindrepBottomNavigationBar`: Docked bottom bar (`Today`, `Routines`, `Progress`) where the `14.dp` active Sage pill wraps **both** the icon and text label.
  - `RoutineSummaryCard`: Equal card hierarchy across all active routines, capped at **4 visible exercises** (`RoutineOverview.MAXIMUM_VISIBLE_EXERCISES`) with `+ N more exercise(s)...` ellipsis when greater than 4.
- **Guided Session Player:** Full-screen Landscape (`ScreenOrientationEffect(landscape = true)` + `KeepScreenAwakeEffect(enabled = true)`). Info and Pause overlays stay in Landscape alongside the dimmed stage.
- **Mascots & Placeholders:** Pure 2B Minimal Gentle Bunny (`KindrepBunnyMascot` — no nose, curved eyes, `6.2f` radius terracotta tail; Dark Mode `#F2EEE6` fill + `#648977` sage border, zero black outlines). Exercise placeholder figures use calm neutral gray (`#94989B` Light / `#D0D4D6` Dark) with terracotta active-limb highlights.
