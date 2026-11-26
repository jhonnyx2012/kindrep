package com.jhonnyb.kindrep.domain.model

enum class ExerciseSide {
    LEFT,
    RIGHT,
}

enum class ExercisePhase {
    EXTEND,
    HOLD,
    RETURN,
}

enum class ExerciseScaleGroup {
    FLOOR,
    VERTICAL,
}

enum class ExerciseBilateralMode {
    NONE,
    OPPOSITE_LIMB_SWAP,
    HORIZONTAL_MIRROR,
}

enum class ExerciseStageProp {
    FLOOR_MAT,
    PERSPECTIVE_MAT,
    PERSPECTIVE_BALL,
    ISOMETRIC_CORNER_MAT,
    WALL_LEFT,
    BENCH_SEATED,
    BENCH_REAR,
    DUMBBELLS,
}

enum class ExerciseBodyPart {
    NECK,
    SPINE,
    NEAR_ARM,
    FAR_ARM,
    NEAR_THIGH,
    NEAR_SHIN,
    FAR_THIGH,
    FAR_SHIN,
}

enum class ExerciseBreathTarget {
    NONE,
    SPINE,
    HIP,
}

data class JointPoint(
    val x: Float,
    val y: Float,
)

data class BodyPose(
    val head: JointPoint,
    val shoulder: JointPoint,
    val spineControl: JointPoint,
    val hip: JointPoint,
    val nearElbow: JointPoint,
    val nearWrist: JointPoint,
    val farElbow: JointPoint,
    val farWrist: JointPoint,
    val nearKnee: JointPoint,
    val nearAnkle: JointPoint,
    val farKnee: JointPoint,
    val farAnkle: JointPoint,
    val nearFoot: JointPoint = JointPoint(nearAnkle.x - DEFAULT_FOOT_LENGTH, nearAnkle.y + 1.5f),
    val farFoot: JointPoint = JointPoint(farAnkle.x - DEFAULT_FOOT_LENGTH, farAnkle.y + 1.5f),
) {
    fun swappedNearAndFar(): BodyPose = copy(
        nearElbow = farElbow,
        nearWrist = farWrist,
        farElbow = nearElbow,
        farWrist = nearWrist,
        nearKnee = farKnee,
        nearAnkle = farAnkle,
        farKnee = nearKnee,
        farAnkle = nearAnkle,
        nearFoot = farFoot,
        farFoot = nearFoot,
    )

    companion object {
        const val DEFAULT_FOOT_LENGTH: Float = 10.5f
    }
}

data class ExerciseAnimationSpec(
    val mediaKey: String,
    val scaleGroup: ExerciseScaleGroup,
    val bilateralMode: ExerciseBilateralMode,
    val stageProp: ExerciseStageProp,
    val breathTarget: ExerciseBreathTarget,
    val restPose: BodyPose,
    val peakPose: BodyPose,
    val activeBodyParts: Set<ExerciseBodyPart>,
    val lockNeutralNeck: Boolean = false,
)

data class StageBoundingBox(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float,
) {
    val centerX: Float
        get() = left + width * 0.5f
}

data class EvaluatedExerciseFrame(
    val pose: BodyPose,
    val activeBodyParts: Set<ExerciseBodyPart>,
    val phase: ExercisePhase,
    val holdProgress: Float,
    val holdSpanRatio: Float,
    val shouldMirrorHorizontally: Boolean,
)

data class GuidedExerciseItem(
    val identifier: String,
    val mediaKey: String,
    val targetRepetitions: Int,
    val sets: Int,
    val repetitionPaceSeconds: Float,
    val peakHoldRatio: Float,
    val isBilateral: Boolean,
) {
    val singleSideDurationSeconds: Int
        get() = kotlin.math.ceil(targetRepetitions * repetitionPaceSeconds).toInt().coerceAtLeast(1)

    val singleSetDurationSeconds: Int
        get() = if (isBilateral) {
            singleSideDurationSeconds * 2 + SWITCH_SIDES_TRANSITION_SECONDS
        } else {
            singleSideDurationSeconds
        }

    fun totalDurationSeconds(betweenSetBreakSeconds: Int = 0): Int {
        val safeSets = sets.coerceAtLeast(1)
        return singleSetDurationSeconds * safeSets + (safeSets - 1) * betweenSetBreakSeconds.coerceAtLeast(0)
    }

    companion object {
        const val SWITCH_SIDES_TRANSITION_SECONDS: Int = 3
    }
}
