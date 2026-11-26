package com.jhonnyb.kindrep.ui.animation

import com.jhonnyb.kindrep.domain.model.BodyPose
import com.jhonnyb.kindrep.domain.model.ExerciseAnimationSpec
import com.jhonnyb.kindrep.domain.model.ExerciseBilateralMode
import com.jhonnyb.kindrep.domain.model.ExerciseBodyPart
import com.jhonnyb.kindrep.domain.model.ExerciseBreathTarget
import com.jhonnyb.kindrep.domain.model.ExerciseScaleGroup
import com.jhonnyb.kindrep.domain.model.ExerciseStageProp
import com.jhonnyb.kindrep.domain.model.GuidedExerciseItem
import com.jhonnyb.kindrep.domain.model.JointPoint
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
}

object ExerciseChoreographyCatalog {
    private val quadrupedBasePose = BodyPose(
        head = JointPoint(221f, 96f),
        shoulder = JointPoint(198f, 103f),
        spineControl = JointPoint(162f, 103f),
        hip = JointPoint(126f, 104f),
        nearElbow = JointPoint(199f, 127f),
        nearWrist = JointPoint(200f, 152f),
        farElbow = JointPoint(194f, 125f),
        farWrist = JointPoint(195f, 150f),
        nearKnee = JointPoint(128f, 152f),
        nearAnkle = JointPoint(96f, 153f),
        farKnee = JointPoint(123f, 150f),
        farAnkle = JointPoint(92f, 151f),
    )

    private val supineBasePose = BodyPose(
        head = JointPoint(236f, 143f),
        shoulder = JointPoint(208f, 147f),
        spineControl = JointPoint(178f, 149f),
        hip = JointPoint(148f, 148f),
        nearElbow = JointPoint(182f, 151f),
        nearWrist = JointPoint(154f, 152f),
        farElbow = JointPoint(184f, 149f),
        farWrist = JointPoint(158f, 150f),
        nearKnee = JointPoint(108f, 108f),
        nearAnkle = JointPoint(88f, 152f),
        farKnee = JointPoint(112f, 106f),
        farAnkle = JointPoint(92f, 150f),
    )

    private val clamshellRestPose = BodyPose(
        head = JointPoint(224f, 98f),
        shoulder = JointPoint(202f, 110f),
        spineControl = JointPoint(174f, 112f),
        hip = JointPoint(146f, 113f),
        farElbow = JointPoint(246f, 126f),
        farWrist = JointPoint(235f, 95f),
        nearElbow = JointPoint(172f, 111f),
        nearWrist = JointPoint(184f, 143f),
        nearKnee = JointPoint(120f, 144f),
        nearAnkle = JointPoint(82f, 118f),
        farKnee = JointPoint(121f, 149f),
        farAnkle = JointPoint(83f, 122f),
    )

    val allSpecs: List<ExerciseAnimationSpec> = listOf(
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.CAT_COW,
            scaleGroup = ExerciseScaleGroup.FLOOR,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.FLOOR_MAT,
            breathTarget = ExerciseBreathTarget.SPINE,
            restPose = quadrupedBasePose.copy(
                head = JointPoint(215f, 119f),
                shoulder = JointPoint(196f, 106f),
                spineControl = JointPoint(160f, 75f),
                hip = JointPoint(124f, 106f),
            ),
            peakPose = quadrupedBasePose.copy(
                head = JointPoint(223f, 89f),
                shoulder = JointPoint(200f, 104f),
                spineControl = JointPoint(162f, 126f),
                hip = JointPoint(124f, 103f),
            ),
            activeBodyParts = setOf(ExerciseBodyPart.SPINE),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.BIRD_DOG,
            scaleGroup = ExerciseScaleGroup.FLOOR,
            bilateralMode = ExerciseBilateralMode.OPPOSITE_LIMB_SWAP,
            stageProp = ExerciseStageProp.FLOOR_MAT,
            breathTarget = ExerciseBreathTarget.NONE,
            restPose = quadrupedBasePose,
            peakPose = quadrupedBasePose.copy(
                nearElbow = JointPoint(232f, 100f),
                nearWrist = JointPoint(266f, 98f),
                farKnee = JointPoint(90f, 103f),
                farAnkle = JointPoint(54f, 101f),
            ),
            activeBodyParts = setOf(
                ExerciseBodyPart.NEAR_ARM,
                ExerciseBodyPart.FAR_THIGH,
                ExerciseBodyPart.FAR_SHIN,
            ),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.GLUTE_BRIDGE,
            scaleGroup = ExerciseScaleGroup.FLOOR,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.FLOOR_MAT,
            breathTarget = ExerciseBreathTarget.HIP,
            restPose = supineBasePose,
            peakPose = supineBasePose.copy(
                hip = JointPoint(154f, 125f),
                spineControl = JointPoint(181f, 136f),
            ),
            activeBodyParts = setOf(
                ExerciseBodyPart.SPINE,
                ExerciseBodyPart.NEAR_THIGH,
                ExerciseBodyPart.FAR_THIGH,
            ),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.DEAD_BUG,
            scaleGroup = ExerciseScaleGroup.FLOOR,
            bilateralMode = ExerciseBilateralMode.OPPOSITE_LIMB_SWAP,
            stageProp = ExerciseStageProp.FLOOR_MAT,
            breathTarget = ExerciseBreathTarget.NONE,
            restPose = supineBasePose.copy(
                nearElbow = JointPoint(208f, 119f),
                nearWrist = JointPoint(208f, 90f),
                farElbow = JointPoint(204f, 119f),
                farWrist = JointPoint(204f, 90f),
                nearKnee = JointPoint(148f, 108f),
                nearAnkle = JointPoint(112f, 108f),
                farKnee = JointPoint(144f, 106f),
                farAnkle = JointPoint(108f, 106f),
            ),
            peakPose = supineBasePose.copy(
                nearElbow = JointPoint(236f, 140f),
                nearWrist = JointPoint(264f, 136f),
                farElbow = JointPoint(204f, 119f),
                farWrist = JointPoint(204f, 90f),
                nearKnee = JointPoint(148f, 108f),
                nearAnkle = JointPoint(112f, 108f),
                farKnee = JointPoint(110f, 138f),
                farAnkle = JointPoint(74f, 136f),
            ),
            activeBodyParts = setOf(
                ExerciseBodyPart.SPINE,
                ExerciseBodyPart.NEAR_ARM,
                ExerciseBodyPart.FAR_THIGH,
                ExerciseBodyPart.FAR_SHIN,
            ),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.CHILDS_POSE,
            scaleGroup = ExerciseScaleGroup.FLOOR,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.FLOOR_MAT,
            breathTarget = ExerciseBreathTarget.SPINE,
            restPose = BodyPose(
                head = JointPoint(170f, 72f),
                shoulder = JointPoint(156f, 90f),
                spineControl = JointPoint(132f, 108f),
                hip = JointPoint(112f, 134f),
                nearElbow = JointPoint(168f, 118f),
                nearWrist = JointPoint(184f, 150f),
                farElbow = JointPoint(164f, 116f),
                farWrist = JointPoint(180f, 148f),
                nearKnee = JointPoint(142f, 152f),
                nearAnkle = JointPoint(94f, 153f),
                farKnee = JointPoint(138f, 150f),
                farAnkle = JointPoint(90f, 151f),
            ),
            peakPose = BodyPose(
                head = JointPoint(202f, 140f),
                shoulder = JointPoint(182f, 130f),
                spineControl = JointPoint(142f, 118f),
                hip = JointPoint(102f, 140f),
                nearElbow = JointPoint(216f, 147f),
                nearWrist = JointPoint(252f, 152f),
                farElbow = JointPoint(212f, 145f),
                farWrist = JointPoint(246f, 150f),
                nearKnee = JointPoint(142f, 152f),
                nearAnkle = JointPoint(94f, 153f),
                farKnee = JointPoint(138f, 150f),
                farAnkle = JointPoint(90f, 151f),
            ),
            activeBodyParts = setOf(ExerciseBodyPart.SPINE, ExerciseBodyPart.NEAR_ARM),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.CLAMSHELL,
            scaleGroup = ExerciseScaleGroup.FLOOR,
            bilateralMode = ExerciseBilateralMode.HORIZONTAL_MIRROR,
            stageProp = ExerciseStageProp.PERSPECTIVE_MAT,
            breathTarget = ExerciseBreathTarget.NONE,
            restPose = clamshellRestPose,
            peakPose = clamshellRestPose.copy(nearKnee = JointPoint(118f, 82f)),
            activeBodyParts = setOf(ExerciseBodyPart.NEAR_THIGH, ExerciseBodyPart.NEAR_SHIN),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.WALL_SLIDE,
            scaleGroup = ExerciseScaleGroup.VERTICAL,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.WALL_LEFT,
            breathTarget = ExerciseBreathTarget.NONE,
            restPose = BodyPose(
                head = JointPoint(144f, 40f),
                shoulder = JointPoint(138f, 60f),
                spineControl = JointPoint(136f, 80f),
                hip = JointPoint(138f, 100f),
                nearElbow = JointPoint(154f, 80f),
                nearWrist = JointPoint(162f, 54f),
                farElbow = JointPoint(148f, 78f),
                farWrist = JointPoint(156f, 52f),
                nearKnee = JointPoint(144f, 127f),
                nearAnkle = JointPoint(146f, 153f),
                farKnee = JointPoint(139f, 126f),
                farAnkle = JointPoint(141f, 152f),
            ),
            peakPose = BodyPose(
                head = JointPoint(144f, 40f),
                shoulder = JointPoint(138f, 58f),
                spineControl = JointPoint(136f, 80f),
                hip = JointPoint(138f, 100f),
                nearElbow = JointPoint(150f, 46f),
                nearWrist = JointPoint(154f, 22f),
                farElbow = JointPoint(145f, 45f),
                farWrist = JointPoint(149f, 21f),
                nearKnee = JointPoint(144f, 127f),
                nearAnkle = JointPoint(146f, 153f),
                farKnee = JointPoint(139f, 126f),
                farAnkle = JointPoint(141f, 152f),
            ),
            activeBodyParts = setOf(ExerciseBodyPart.NEAR_ARM, ExerciseBodyPart.FAR_ARM),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.CHIN_TUCK,
            scaleGroup = ExerciseScaleGroup.VERTICAL,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.BENCH_SEATED,
            breathTarget = ExerciseBreathTarget.NONE,
            restPose = BodyPose(
                head = JointPoint(156f, 44f),
                shoulder = JointPoint(138f, 68f),
                spineControl = JointPoint(136f, 92f),
                hip = JointPoint(138f, 116f),
                nearElbow = JointPoint(142f, 94f),
                nearWrist = JointPoint(164f, 111f),
                farElbow = JointPoint(139f, 92f),
                farWrist = JointPoint(160f, 109f),
                nearKnee = JointPoint(178f, 117f),
                nearAnkle = JointPoint(178f, 153f),
                farKnee = JointPoint(173f, 116f),
                farAnkle = JointPoint(173f, 151f),
            ),
            peakPose = BodyPose(
                head = JointPoint(141f, 41f),
                shoulder = JointPoint(136f, 68f),
                spineControl = JointPoint(135f, 92f),
                hip = JointPoint(138f, 116f),
                nearElbow = JointPoint(142f, 94f),
                nearWrist = JointPoint(164f, 111f),
                farElbow = JointPoint(139f, 92f),
                farWrist = JointPoint(160f, 109f),
                nearKnee = JointPoint(178f, 117f),
                nearAnkle = JointPoint(178f, 153f),
                farKnee = JointPoint(173f, 116f),
                farAnkle = JointPoint(173f, 151f),
            ),
            activeBodyParts = setOf(ExerciseBodyPart.NECK),
        ),
        ExerciseAnimationSpec(
            mediaKey = ExerciseMediaKeys.BULGARIAN_SPLIT_SQUAT,
            scaleGroup = ExerciseScaleGroup.VERTICAL,
            bilateralMode = ExerciseBilateralMode.HORIZONTAL_MIRROR,
            stageProp = ExerciseStageProp.BENCH_REAR,
            breathTarget = ExerciseBreathTarget.NONE,
            restPose = BodyPose(
                head = JointPoint(164f, 28f),
                shoulder = JointPoint(158f, 48f),
                spineControl = JointPoint(154f, 68f),
                hip = JointPoint(150f, 88f),
                nearElbow = JointPoint(144f, 69f),
                nearWrist = JointPoint(157f, 84f),
                farElbow = JointPoint(149f, 68f),
                farWrist = JointPoint(161f, 83f),
                nearKnee = JointPoint(167f, 121f),
                nearAnkle = JointPoint(170f, 153f),
                farKnee = JointPoint(132f, 120f),
                farAnkle = JointPoint(104f, 115f),
            ),
            peakPose = BodyPose(
                head = JointPoint(168f, 53f),
                shoulder = JointPoint(161f, 73f),
                spineControl = JointPoint(154f, 93f),
                hip = JointPoint(146f, 112f),
                nearElbow = JointPoint(144f, 93f),
                nearWrist = JointPoint(156f, 108f),
                farElbow = JointPoint(149f, 92f),
                farWrist = JointPoint(160f, 107f),
                nearKnee = JointPoint(180f, 119f),
                nearAnkle = JointPoint(170f, 153f),
                farKnee = JointPoint(132f, 146f),
                farAnkle = JointPoint(104f, 115f),
            ),
            activeBodyParts = setOf(ExerciseBodyPart.NEAR_THIGH, ExerciseBodyPart.NEAR_SHIN),
        ),
    )

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
            identifier = "ex-cat-cow",
            mediaKey = ExerciseMediaKeys.CAT_COW,
            targetRepetitions = 10,
            sets = 1,
            repetitionPaceSeconds = 6.2f,
            peakHoldRatio = 0.24f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-bird-dog",
            mediaKey = ExerciseMediaKeys.BIRD_DOG,
            targetRepetitions = 8,
            sets = 2,
            repetitionPaceSeconds = 6.5f,
            peakHoldRatio = 0.36f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-glute-bridge",
            mediaKey = ExerciseMediaKeys.GLUTE_BRIDGE,
            targetRepetitions = 12,
            sets = 2,
            repetitionPaceSeconds = 6.4f,
            peakHoldRatio = 0.40f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-dead-bug",
            mediaKey = ExerciseMediaKeys.DEAD_BUG,
            targetRepetitions = 8,
            sets = 2,
            repetitionPaceSeconds = 6.8f,
            peakHoldRatio = 0.32f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-childs-pose",
            mediaKey = ExerciseMediaKeys.CHILDS_POSE,
            targetRepetitions = 3,
            sets = 1,
            repetitionPaceSeconds = 8.5f,
            peakHoldRatio = 0.62f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-clamshell",
            mediaKey = ExerciseMediaKeys.CLAMSHELL,
            targetRepetitions = 12,
            sets = 2,
            repetitionPaceSeconds = 5.8f,
            peakHoldRatio = 0.36f,
            isBilateral = true,
        ),
        GuidedExerciseItem(
            identifier = "ex-wall-slide",
            mediaKey = ExerciseMediaKeys.WALL_SLIDE,
            targetRepetitions = 10,
            sets = 2,
            repetitionPaceSeconds = 6.4f,
            peakHoldRatio = 0.28f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-chin-tuck",
            mediaKey = ExerciseMediaKeys.CHIN_TUCK,
            targetRepetitions = 10,
            sets = 2,
            repetitionPaceSeconds = 6.0f,
            peakHoldRatio = 0.44f,
            isBilateral = false,
        ),
        GuidedExerciseItem(
            identifier = "ex-bulgarian-split-squat",
            mediaKey = ExerciseMediaKeys.BULGARIAN_SPLIT_SQUAT,
            targetRepetitions = 8,
            sets = 2,
            repetitionPaceSeconds = 6.4f,
            peakHoldRatio = 0.30f,
            isBilateral = true,
        ),
    )
}
