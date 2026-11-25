---
trigger: glob
globs: "**/ui/**/*.kt, **/navigation/**/*.kt, **/*Root.kt"
description: "Legible Composable decomposition, Compose recomposition & phase-deferral mastery, localization, single @Preview rule, and Kindrep design system invariants."
---

# Compose Multiplatform Decomposition, Recomposition & Design System Rules

## 1. Legible Composable Decomposition & File Organization
- **Top-Level Composables Read Like an Outline:** Keep top-level screen and card composables concise (~25–50 lines) so their layout hierarchy is immediately scannable. Extract distinct visual sections (headers, badges, hero blocks, lists, action footers) into small, descriptive **sub-composables**—even when used only once!
- **Cohesive Placement:**
  - Keep private, single-screen/single-card sub-composables in the **same file** right below the parent composable they serve.
  - Promote a component to its own file in `ui/components/` (or `ui/dialogs/`) as soon as it is shared across screens or represents an independent domain widget (`RoutineSummaryCard`, `CompanionStreakCard`, `KindrepBottomNavigationBar`, `KindrepBunnyMascot`).
  - Never bundle multiple screens or unrelated public widgets into one file (max 700 lines per file).
- **Exactly One `@Preview` Per Visual File:** Every file under `ui/components/`, `ui/dialogs/`, and `ui/screens/` must declare **exactly one** `@Preview` function wrapped in `KindrepTheme`. Guard native views (`LoopingExerciseVideo`) with `LocalInspectionMode.current` so previews always render deterministically.

## 2. Landscape, Tablet & Window-Inset Compatibility (Mandatory)
Every screen in Kindrep must feel natural in **both Portrait and Landscape** across phones, foldables, and tablets—even before dedicated multi-pane tablet layouts exist:
1. **Never Lock Non-Player Screens to Portrait:** All standard screens (`Welcome`, `Today`, `Routines`, `Progress`, `RoutineEditor`, `ExerciseLibraryPicker`, `PostSessionCheckIn`) must rotate freely (`SCREEN_ORIENTATION_UNSPECIFIED`). Only the Guided Session Player invokes `PreferLandscapeOrientationEffect()`, restoring `UNSPECIFIED` on exit (`:composeApp:verifyUiArchitecture` forbids `SCREEN_ORIENTATION_PORTRAIT`).
2. **Vertical Scroll Resilience Everywhere:** Landscape phones and split-screen tablets often have `< 400.dp` of vertical height. Every screen in `ui/screens/` **must** support vertical scrolling (`ResponsiveScrollableColumn`, `verticalScroll`, or `LazyColumn`)—never trap primary actions below a non-scrollable `Column` or use `Spacer(Modifier.weight(1f))` without bounded/scrollable safety (`:composeApp:verifyUiArchitecture` enforces scroll support in every screen file).
3. **Centered Readable Max-Width (`ResponsiveScrollableColumn`):** Never stretch single-column cards, forms, primary buttons, or bottom navigation pills full-bleed across `1000.dp+` tablet widths. Constrain single-column screen content to a centered `maxWidth` (`560.dp–640.dp` via `ResponsiveScrollableColumn`, keeping scroll gestures active across the outer full-width container) and cap inner bottom-bar tab rows similarly.
4. **Safe Drawing & Display Cutout Insets:** Respect `WindowInsets.safeDrawing` (including **horizontal** insets in landscape so camera cutouts and system bars never overlap headers, cards, or navigation bars).

## 3. Recomposition & Rendering Performance Guardrails
Avoid unnecessary recompositions and frame drops across Android and iOS:
1. **Stable Inputs Only:** Pass `@Immutable` / `@Stable` UI states, primitives, or `:domain` models (trusted via `compose_stability.conf`). Never pass `MutableList`, `Flow`, `StateFlow`, or `ViewModel` instances into stateless screen or component composables—hoist `collectAsStateWithLifecycle()` to the route boundary.
2. **Defer High-Frequency State Reads to Layout or Draw Phases:**
   - Never read rapidly changing `State<Float>` / `State<Int>` values (animations, scroll offsets, countdown progress) directly in a parent composable body if only Layout or Draw depends on them.
   - For animations in `Canvas`, hold the `State<Float>` reference (`val animationProgress = transition.animateFloat(...)`) and read `animationProgress.value` **inside `Canvas { ... }` (`DrawScope`)** so Compose skips Composition and Layout completely on every frame.
   - For positional or visual transforms, prefer lambda modifiers (`Modifier.graphicsLayer { ... }`, `Modifier.offset { ... }`, `Modifier.drawBehind { ... }`) over value modifiers (`Modifier.alpha(x)`, `Modifier.offset(x.dp)`).
   - For ticking timers or rep counters, pass lambda state providers (`remainingSecondsProvider: () -> Int`) or isolate the text read inside a tiny leaf composable so surrounding video players and controls never recompose on every tick.
3. **Zero Per-Frame Allocations in `DrawScope`:** Never allocate `Path()`, `Stroke()`, `Brush`, or collections inside `Canvas` / `drawBehind`. Allocate mutable `Path` objects once via `remember { Path() }` and call `path.reset()` inside `DrawScope`, and hoist constant `Stroke` styles to file-level `private val`s.
4. **Derived Thresholds & Lazy List Keys:**
   - Wrap high-frequency-to-boolean derivations (e.g., `listState.firstVisibleItemIndex > 0`) in `remember { derivedStateOf { ... } }`.
   - Every `LazyColumn` / `LazyRow` `items(...)` call **must** specify a stable `key = { it.identifier }` (and `contentType` when heterogeneous) so list updates skip unchanged rows.

## 4. Localization, Naming & Constants
- **100% Localized User Interface Text:** Every visible string in `ui/` and `navigation/` must live in `composeResources/values/strings.xml` and resolve via `stringResource(Res.string.*)`. `:composeApp:verifyUiArchitecture` rejects hardcoded `Text("...")` literals.
- **Expressive, Acronym-Free Naming & Constants:** Use spelled-out identifiers (`centerX`, `canvasWidth`, `routineIdentifier`, `navigationController`) instead of abbreviations (`cx`, `w`, `id`, `nav`). Extract repeatable keys into constant objects only when reused.

## 5. Locked Kindrep Design System Invariants
- **Semantic Palette (`KindrepPalette.kt`):** Read colors via `LocalKindrepPalette.current` (`WarmEarthPalette` Light / `SoftStoneD3Palette` Dark) and shapes/typography via `MaterialTheme`.
- **Typography & Legibility:** Bundled `Nunito` with tabular numerals (`fontFeatureSettings = "tnum"`). Routine card titles use `Bold` (`20–21.sp`), exercise row titles use `Regular` (`16.sp`), minimum `14.sp` labels, and `56.dp+` primary touch targets.
- **Corner Radii:** Modals/sheets `18.dp`, cards / primary buttons / active navigation pill `14.dp`, inner rows/inputs `12.dp`, badges/chips `9.dp`.
- **Domain Copy & Layout Rules:** Display **real numeric exercise values only** (`10 reps`, `8 / side`, `12 reps · 2 sets`, `45s hold` — never `"default"` or `"Edited"` badges). Cap Today's `RoutineSummaryCard` at **4 visible exercises** + ellipsis. Keep the Guided Session Player and its Info/Pause overlays in **Full-Screen Landscape**.
