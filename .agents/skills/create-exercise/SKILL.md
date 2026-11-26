---
name: create-exercise
description: >-
  End-to-end guide for designing, rigging, localizing, and testing new pure-Compose
  14-joint exercises in Kindrep. Use whenever adding a new exercise to the catalog,
  authoring BodyPose keyframes, selecting scale groups, props, or bilateral modes,
  or refining exercise animation readability and copy.
---

# Create Exercise — Kindrep Pure-Compose Authoring Skill

Kindrep renders every exercise natively in `commonMain` via a **14-joint `BodyPose` kinematic engine** (`ExercisePoseEvaluator` — 12 core joints + `nearFoot` / `farFoot`) and a single zero-allocation Compose `Canvas` stage (`ExerciseAnimationStage` + `ExerciseStageDrawHelpers`). Follow this guide whenever adding or refining an exercise so it looks anatomically natural, matches Kindrep's calm clinical aesthetic, and compiles cleanly on Android and Apple Silicon iOS.

---

## 1. Visual & Anatomical Design Rules

### A. Coordinate Space, Grounding & Proportions
* Author poses in Kindrep's **isotropic `320 × 200` logical stage** with the floor mat baseline at **`y = 158f`** (`ExercisePoseEvaluator.FLOOR_MAT_BASELINE_Y`, top surface at `y = 158.25f`).
* **Strict Mat-Surface Grounding (Never Clip Below or Float Above the Mat):**
  * **Head resting flat on mat:** `head.y = 146.5f` (`158.25 - 11.5`)
  * **Torso (`shoulder`, `spineControl`, `hip`) resting flat on mat:** `y = 150.5f` (`158.25 - 7.25`)
  * **Wrists, knees, and ankles resting flat on mat:** `y = 153.5f` (`158.25 - 4.5`)
  * **Flat planted toes (`nearFoot` / `farFoot`):** `y = 155.5f` (`158.25 - 2.1`)
  * Supine/prone bodies must lie **dead-level** (never let `head.y` float high or make the torso look inclined when lying flat).
* **Reuse base postures** in `FloorExerciseChoreographies.kt` (`quadrupedBasePose`, `supineBasePose`, `clamshellRestPose`) via `.copy(...)` whenever possible so the figure reads as the exact same human body across exercises.
* Keep limb proportions consistent:
  * Head radius: `11.5f` (drawn automatically)
  * Foot (`ankle` → `foot`): **Balanced `10.5f`** (`BodyPose.DEFAULT_FOOT_LENGTH`, drawn as a seamless slim flat-sole anatomical taper — thinner than the shin, zero ankle seam). Always orient toes intentionally: pointing **upward** for heel-dig bridges (*Hamstring Bridge*), **planted flat** while heel pivots up for calf/soleus raises, and pointing **forward** in the direction the figure faces.
  * Thigh: `~36–46` units · Shin: `~34–46` units
  * Upper arm: `~24–34` units · Forearm: `~24–34` units
  * Torso (`hip` ↔ `shoulder`): `~52–74` units (curved via quadratic `spineControl`)

### B. Unified Scale Groups (Critical for Professional Consistency)
* Choose **`ExerciseScaleGroup.FLOOR`** for all mat-based exercises (quadruped, supine, prone, kneeling, side-lying).
* Choose **`ExerciseScaleGroup.VERTICAL`** for standing, wall, seated, and split-squat exercises.
* **Why:** All exercises in the same `ExerciseScaleGroup` share one camera scale (`unifiedGroupDimensions`) so shorter poses (like *Glute Bridge*) never zoom in and look larger than taller poses (like *Bird Dog* or *Child's Pose*), while `ExerciseAnimationStage` centers each exercise's own `bounds.centerY` cleanly inside the stage on both phones and tablets.

### C. Open-Chain vs. Closed-Chain Kinematics & Neutral Neck
`ExercisePoseEvaluator` automatically selects the right joint math based on how far a distal joint (`wrist`, `ankle`, or `foot`) travels between `restPose` and `peakPose`:
* **Planted / Closed-Chain (`distance(restEnd, peakEnd) < 3f`):** Distal joint stays rock-solid anchored to the floor/mat/bench/wall, and the intermediate joint (`elbow`/`knee`) interpolates linearly. Use identical `(x, y)` coordinates for planted hands/feet in `restPose` and `peakPose` (*Cat-Cow*, *Child's Pose* stationary palms, *Glute Bridge*, *Soleus Heel Raise* planted toes, *Clamshell*, *Bulgarian Split Squat*).
* **Swinging / Open-Chain (`distance(restEnd, peakEnd) >= 3f`):** The 2-bone chain interpolates along a circular polar arc around its parent joint so limbs preserve rigid length and never shrink or clip through the mat (*Bird Dog*, *Dead Bug*, *Wall Slide*).
* **Rigid Neutral Neck (`lockNeutralNeck = true`):** Always set `lockNeutralNeck = true` on hip-hinge patterns (*Deadlift Hip Hinge*, *Single-Leg Hip Hinge*) so the head and neck stay strictly inline with the `hip → shoulder` spine axis at every frame without craning or drooping.

### D. Perspective & Cross-Body Postures (Mandatory Camera Angle Rule)
* **Never flatten lateral-opening or cross-body exercises into a pure 2D side profile!** In a side profile, left and right shoulders/hips collapse to one point, making lateral abduction or cross-body pulls unreadable.
* **Side-lying / Seated Figure-4 (`PERSPECTIVE_MAT` / `PERSPECTIVE_BALL`):** Use a 3/4 horizontal perspective mat so bent knees and crossed legs project clearly into 2D while resting arms sit naturally (*Clamshell*, *Thoracic Open Book*, *Seated Glute Release*).
* **Cross-Body Supine (`ISOMETRIC_CORNER_MAT`):** Use a 45° isometric corner mat with **separated shoulder & hip width**, swapped foreground/background arm tones, a projected depth shadow under the lifted leg, and a subtle calm sage directional pull arrow (*Piriformis Cross-Body Stretch*).

### E. Props, Bilateral Modes & Highlighting
* **Shared Props Only:** Reuse existing `ExerciseStageProp` values (`FLOOR_MAT`, `PERSPECTIVE_MAT`, `PERSPECTIVE_BALL`, `ISOMETRIC_CORNER_MAT`, `WALL_LEFT` architectural wall panel + baseboard, `BENCH_SEATED`, `BENCH_REAR`, `DUMBBELLS`). Never draw a skinny vertical pole for a wall.
* **Equipment Color Rule:** Equipment props (like the massage ball in `PERSPECTIVE_BALL`) use calm neutral/sage (`palette.primary`) — **never** active terracotta (`palette.secondary`), which is reserved exclusively for active target muscles/limbs.
* **Bilateral Mode:**
  * `ExerciseBilateralMode.OPPOSITE_LIMB_SWAP` — camera stays fixed while near/far limbs swap roles (*Bird Dog*, *Dead Bug*).
  * `ExerciseBilateralMode.HORIZONTAL_MIRROR` — user rolls, turns, or switches sides cleanly (*Clamshell*, *Piriformis Cross-Body Stretch*, *Bulgarian Split Squat*, *Soleus Heel Raise*).
  * `ExerciseBilateralMode.NONE` — symmetric exercises (*Cat-Cow*, *Glute Bridge*, *Child's Pose*, *Deadlift Hip Hinge*).
* **Active-Limb Highlighting (`activeBodyParts`):** Highlight **only** the primary moving limbs or spine (`palette.secondary` terracotta). Keep stabilizing limbs neutral so the motion reads instantly from 2 meters away. Never introduce custom stroke colors or black outlines; `ExerciseAnimationStage` handles the thin `palette.figureOutline` and soft shadows automatically across *Warm Earth* and *Soft Stone D3*.
* **Breathing Micro-Motion (`breathTarget`):** Set `SPINE` or `HIP` only on slow mobility/hold exercises (*Cat-Cow*, *Child's Pose*, *Glute Bridge*) where a gentle hold swell feels natural.

---

## 2. Calm Copy & Localization Rules

* **Zero Escaped Apostrophes (`\'`) in Compose Multiplatform XML:** Unlike legacy Android `res/values/strings.xml`, `composeApp/src/commonMain/composeResources/values/strings.xml` renders `\'` literally on screen. Always write plain apostrophes (`Child's Pose`, `practitioner's`, `doesn't`).
* **Distance Mode Copy Budget (Floor-Readable & Unhurried):**
  * `title`: Clear, standard clinical name (e.g., `Bulgarian Split Squat`).
  * `steady_cue`: **One concise sentence (~45–65 chars)** that stays steady on screen for the whole exercise—never cram multi-step paragraphs into the live player.
  * `phase_extend`, `phase_hold`, `phase_return`: **1–2 short words** each (e.g., `Lower slowly`, `Hold`, `Press up`). The player dissolves phase labels slowly (`950ms`) with a minimum `1,000ms` interval.
  * `setup` & `breathing`: Slightly richer 1-sentence guidance shown during the **10s Up Next Break** and inside the **auto-paused Info Sheet**.
* **Non-Prescription Safety Tone:** Describe calm, pain-free movement mechanics only. Never diagnose injuries or claim medical outcomes.
* **Calm Default Pacing:** Aim for `5.8f–6.8f` seconds per repetition (`~8.5f` for deep stretch holds) and `peakHoldRatio` between `0.24f` and `0.44f` (`~0.60f` for hold-focused poses).

---

## 3. Step-by-Step Implementation Checklist

1. **Prototype Complex Postures First (Optional but Recommended):** For unfamiliar 3D angles, side-lying postures, or new props, prototype the `restPose` → `peakPose` motion first (e.g., in an inline HTML canvas widget or by previewing `ExerciseAnimationStage`) before finalizing coordinates.
2. **Add Animation Spec:**
   * Add a `const val` key in `ExerciseMediaKeys` inside `composeApp/src/commonMain/kotlin/com/jhonnyb/kindrep/ui/animation/ExerciseChoreographyCatalog.kt`.
   * Add the `ExerciseAnimationSpec` entry to `ExerciseChoreographyCatalog.allSpecs`.
   * If adding to the sample session, add a `GuidedExerciseItem` in `sampleLowerBackSessionExercises()`.
3. **Add Localized Strings (7 Resources):**
   * In `composeApp/src/commonMain/composeResources/values/strings.xml`, add:
     * `exercise_<key>_title`
     * `exercise_<key>_steady_cue`
     * `exercise_<key>_setup`
     * `exercise_<key>_breathing`
     * `exercise_<key>_phase_extend`
     * `exercise_<key>_phase_hold`
     * `exercise_<key>_phase_return`
4. **Map Copy Resources:**
   * Register the `ExerciseCopyResources` entry in `composeApp/src/commonMain/kotlin/com/jhonnyb/kindrep/ui/animation/ExerciseCopyCatalog.kt`.
5. **Add / Update Unit Tests:**
   * Verify the new `mediaKey` resolves in `ExerciseChoreographyCatalogTest.kt` (both `ExerciseChoreographyCatalog.resolveSpec` and `ExerciseCopyCatalog.resolve`).
   * If adding a new kinematic rule or prop, add behavior tests in `ExercisePoseEvaluatorTest.kt`.
6. **Verify Build, Architecture & Visuals:**
   * Run `powershell -ExecutionPolicy Bypass -File .\scripts\verify.ps1` (or `./gradlew :domain:verifyDomainImmutability :domain:jvmTest :data:testAndroidHostTest :composeApp:verifyUiArchitecture :composeApp:testAndroidHostTest :androidApp:assembleDebug --no-daemon`).
   * Visually check both **Warm Earth (Light)** and **Soft Stone D3 (Dark)** in `ExerciseAnimationStagePreview` or on a phone/tablet emulator—confirming limb separation, no floor clipping, centered vertical framing, and clean apostrophe rendering.
