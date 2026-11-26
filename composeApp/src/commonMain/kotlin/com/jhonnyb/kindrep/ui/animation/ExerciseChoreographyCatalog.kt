package com.jhonnyb.kindrep.ui.animation

import com.jhonnyb.kindrep.domain.model.ExerciseAnimationSpec
import com.jhonnyb.kindrep.domain.model.ExerciseScaleGroup
import com.jhonnyb.kindrep.domain.model.GuidedExerciseItem
import com.jhonnyb.kindrep.domain.model.StageBoundingBox
import com.jhonnyb.kindrep.domain.usecase.ExercisePoseEvaluator

object ExerciseMediaKeys {
    const val CAT_COW: String = "cat_cow"
    const val BIRD_DOG: String = "bird_dog"
    const val GLUTE_BRIDGE: String = "glute_bridge"
    const val DEAD_BUG: String = "dead_bug"
    const val CHILDS_POSE: String = "childs_pose"
    const val CLAMSHELL: String = "clamshell"
    const val WALL_SLIDE: String = "wall_slide"
    const val CHIN_TUCK: String = "chin_tuck"
    const val BULGARIAN_SPLIT_SQUAT: String = "bulgarian_split_squat"
    const val KNEE_TO_CHEST: String = "knee_to_chest"
    const val HEEL_SLIDES: String = "heel_slides"
    const val STRAIGHT_LEG_RAISE: String = "straight_leg_raise"
    const val PRONE_PRESS_UP: String = "prone_press_up"
    const val HALF_KNEELING_HIP_FLEXOR: String = "hip_flexor_stretch"
    const val MODIFIED_SIDE_PLANK: String = "modified_side_plank"
    const val CHAIR_SIT_TO_STAND: String = "chair_sit_to_stand"
    const val SEATED_KNEE_EXTENSION: String = "seated_knee_extension"
    const val STANDING_CALF_RAISE: String = "standing_calf_raise"
    const val THORACIC_OPEN_BOOK: String = "thoracic_open_book"
    const val PELVIC_TILT: String = "pelvic_tilt"
    const val MCGILL_CURL_UP: String = "mcgill_curl_up"
    const val HAMSTRING_BRIDGE: String = "hamstring_bridge"
    const val LYING_LEG_CURL: String = "lying_leg_curl"
    const val SINGLE_LEG_HIP_HINGE: String = "single_leg_hip_hinge"
    const val SOLEUS_HEEL_RAISE: String = "soleus_heel_raise"
    const val WALL_CALF_STRETCH: String = "wall_calf_stretch"
    const val DEADLIFT_HIP_HINGE: String = "deadlift_hip_hinge"
    const val PIRIFORMIS_STRETCH: String = "piriformis_stretch"
    const val SEATED_GLUTE_RELEASE: String = "seated_glute_release"
}

object ExerciseChoreographyCatalog {
    val allSpecs: List<ExerciseAnimationSpec> = floorExerciseSpecs + verticalExerciseSpecs

    private val specsByMediaKey: Map<String, ExerciseAnimationSpec> =
        allSpecs.associateBy { it.mediaKey }

    private val sampledBoundsByMediaKey: Map<String, StageBoundingBox> =
        allSpecs.associate { spec -> spec.mediaKey to ExercisePoseEvaluator.computeSampledBounds(spec) }

    val unifiedGroupDimensions: Map<ExerciseScaleGroup, Pair<Float, Float>> =
        ExercisePoseEvaluator.computeUnifiedGroupDimensions(allSpecs)

    fun resolveSpec(mediaKey: String): ExerciseAnimationSpec =
        specsByMediaKey[mediaKey] ?: specsByMediaKey.getValue(ExerciseMediaKeys.BIRD_DOG)

    fun resolveBounds(mediaKey: String): StageBoundingBox =
        sampledBoundsByMediaKey[mediaKey]
            ?: sampledBoundsByMediaKey.getValue(ExerciseMediaKeys.BIRD_DOG)

    fun sampleLowerBackSessionExercises(): List<GuidedExerciseItem> = listOf(
        GuidedExerciseItem(
            identifier = "ex-cat-camel",
            mediaKey = ExerciseMediaKeys.CAT_COW,
            targetRepetitions = 5,
            sets = 2,
            repetitionPaceSeconds = 10.0f,
            peakHoldRatio = 0.77f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-childs-pose",
            mediaKey = ExerciseMediaKeys.CHILDS_POSE,
            targetRepetitions = 1,
            sets = 2,
            repetitionPaceSeconds = 46.0f,
            peakHoldRatio = 1.0f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-mcgill-curl-up",
            mediaKey = ExerciseMediaKeys.MCGILL_CURL_UP,
            targetRepetitions = 10,
            sets = 3,
            repetitionPaceSeconds = 10.0f,
            peakHoldRatio = 0.77f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-bridge-pelvic-tilt",
            mediaKey = ExerciseMediaKeys.GLUTE_BRIDGE,
            targetRepetitions = 10,
            sets = 3,
            repetitionPaceSeconds = 10.0f,
            peakHoldRatio = 0.77f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-hamstring-bridge",
            mediaKey = ExerciseMediaKeys.HAMSTRING_BRIDGE,
            targetRepetitions = 6,
            sets = 3,
            repetitionPaceSeconds = 10.0f,
            peakHoldRatio = 0.77f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-lying-leg-curl",
            mediaKey = ExerciseMediaKeys.LYING_LEG_CURL,
            targetRepetitions = 12,
            sets = 3,
            repetitionPaceSeconds = 6.0f,
            peakHoldRatio = 0.52f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-single-leg-hip-hinge",
            mediaKey = ExerciseMediaKeys.SINGLE_LEG_HIP_HINGE,
            targetRepetitions = 12,
            sets = 3,
            repetitionPaceSeconds = 5.0f,
            peakHoldRatio = 0.35f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-soleus-heel-raise",
            mediaKey = ExerciseMediaKeys.SOLEUS_HEEL_RAISE,
            targetRepetitions = 15,
            sets = 3,
            repetitionPaceSeconds = 4.0f,
            peakHoldRatio = 0.30f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-standing-calf-raise",
            mediaKey = ExerciseMediaKeys.STANDING_CALF_RAISE,
            targetRepetitions = 15,
            sets = 3,
            repetitionPaceSeconds = 4.0f,
            peakHoldRatio = 0.30f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-wall-calf-stretch",
            mediaKey = ExerciseMediaKeys.WALL_CALF_STRETCH,
            targetRepetitions = 1,
            sets = 3,
            repetitionPaceSeconds = 46.0f,
            peakHoldRatio = 1.0f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-deadlift-hip-hinge",
            mediaKey = ExerciseMediaKeys.DEADLIFT_HIP_HINGE,
            targetRepetitions = 12,
            sets = 3,
            repetitionPaceSeconds = 5.0f,
            peakHoldRatio = 0.35f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-piriformis-stretch",
            mediaKey = ExerciseMediaKeys.PIRIFORMIS_STRETCH,
            targetRepetitions = 1,
            sets = 3,
            repetitionPaceSeconds = 46.0f,
            peakHoldRatio = 1.0f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-seated-glute-release",
            mediaKey = ExerciseMediaKeys.SEATED_GLUTE_RELEASE,
            targetRepetitions = 1,
            sets = 3,
            repetitionPaceSeconds = 46.0f,
            peakHoldRatio = 1.0f,
            isBilateral = true,
        ),
    )
}
