package com.jhonnyb.kindrep.domain.usecase

import com.jhonnyb.kindrep.domain.model.BodyPose
import com.jhonnyb.kindrep.domain.model.EvaluatedExerciseFrame
import com.jhonnyb.kindrep.domain.model.ExerciseAnimationSpec
import com.jhonnyb.kindrep.domain.model.ExerciseBilateralMode
import com.jhonnyb.kindrep.domain.model.ExerciseBodyPart
import com.jhonnyb.kindrep.domain.model.ExerciseBreathTarget
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.domain.model.ExerciseScaleGroup
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.ExerciseStageProp
import com.jhonnyb.kindrep.domain.model.JointPoint
import com.jhonnyb.kindrep.domain.model.StageBoundingBox
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin

object ExercisePoseEvaluator {
    const val FLOOR_MAT_BASELINE_Y: Float = 158f
    private const val PLANTED_DISTAL_THRESHOLD: Float = 3.0f
    private const val MAX_HOLD_SPAN_MULTIPLIER: Float = 0.65f
    private const val HEAD_BOUNDING_RADIUS: Float = 12.5f
    private const val LIMB_BOUNDING_RADIUS: Float = 7.5f
    private const val BOUNDING_PADDING: Float = 4.0f
    private const val BREATH_AMPLITUDE: Float = 1.5f

    fun evaluateFrame(
        spec: ExerciseAnimationSpec,
        cycleProgress: Float,
        peakHoldRatio: Float,
        activeSide: ExerciseSide,
    ): EvaluatedExerciseFrame {
        val clampedProgress = cycleProgress.coerceIn(0f, 1f)
        val holdSpan = (peakHoldRatio.coerceIn(0f, 1f)) * MAX_HOLD_SPAN_MULTIPLIER
        val moveSpan = (1f - holdSpan) * 0.5f

        val lift: Float
        val phase: ExercisePhase
        val holdProgress: Float

        when {
            clampedProgress < moveSpan -> {
                lift = if (moveSpan > 0.001f) easeInOutCubic(clampedProgress / moveSpan) else 1f
                phase = ExercisePhase.EXTEND
                holdProgress = 0f
            }

            clampedProgress < moveSpan + holdSpan -> {
                lift = 1f
                phase = ExercisePhase.HOLD
                holdProgress = if (holdSpan > 0.001f) (clampedProgress - moveSpan) / holdSpan else 0f
            }

            else -> {
                val returnFraction = if (moveSpan > 0.001f) {
                    1f - (clampedProgress - moveSpan - holdSpan) / moveSpan
                } else {
                    0f
                }
                lift = easeInOutCubic(returnFraction.coerceIn(0f, 1f))
                phase = ExercisePhase.RETURN
                holdProgress = 0f
            }
        }

        val swapLimbs = spec.bilateralMode == ExerciseBilateralMode.OPPOSITE_LIMB_SWAP &&
            activeSide == ExerciseSide.RIGHT
        val startPose = if (swapLimbs) spec.restPose.swappedNearAndFar() else spec.restPose
        val endPose = if (swapLimbs) spec.peakPose.swappedNearAndFar() else spec.peakPose
        val activeParts = if (swapLimbs) {
            spec.activeBodyParts.map(::swapBodyPart).toSet()
        } else {
            spec.activeBodyParts
        }

        val breathOffset = if (phase == ExercisePhase.HOLD) {
            sin(holdProgress * PI).toFloat() * BREATH_AMPLITUDE
        } else {
            0f
        }

        val rawHip = lerpPoint(startPose.hip, endPose.hip, lift)
        val hip = if (spec.breathTarget == ExerciseBreathTarget.HIP) {
            rawHip.copy(y = rawHip.y - breathOffset * 0.7f)
        } else {
            rawHip
        }
        val shoulder = if (spec.lockNeutralNeck) {
            polarArcStep(
                currentParent = hip,
                startParent = startPose.hip,
                startChild = startPose.shoulder,
                endParent = endPose.hip,
                endChild = endPose.shoulder,
                fraction = lift,
            )
        } else {
            lerpPoint(startPose.shoulder, endPose.shoulder, lift)
        }
        val rawSpineControl = if (spec.lockNeutralNeck) {
            JointPoint(
                x = (hip.x + shoulder.x) * 0.5f,
                y = (hip.y + shoulder.y) * 0.5f,
            )
        } else {
            lerpPoint(startPose.spineControl, endPose.spineControl, lift)
        }
        val spineControl = if (spec.breathTarget == ExerciseBreathTarget.SPINE) {
            rawSpineControl.copy(y = rawSpineControl.y + breathOffset)
        } else {
            rawSpineControl
        }
        val head = if (spec.lockNeutralNeck) {
            val spineDx = shoulder.x - hip.x
            val spineDy = shoulder.y - hip.y
            val spineLength = hypot(spineDx, spineDy).coerceAtLeast(0.001f)
            val neckLength = distance(startPose.shoulder, startPose.head)
            JointPoint(
                x = shoulder.x + (spineDx / spineLength) * neckLength,
                y = shoulder.y + (spineDy / spineLength) * neckLength,
            )
        } else {
            lerpPoint(startPose.head, endPose.head, lift)
        }

        val farArm = interpolateTwoBoneChain(
            currentRoot = shoulder,
            startRoot = startPose.shoulder,
            startMid = startPose.farElbow,
            startEnd = startPose.farWrist,
            endRoot = endPose.shoulder,
            endMid = endPose.farElbow,
            endEnd = endPose.farWrist,
            fraction = lift,
        )
        val nearArm = interpolateTwoBoneChain(
            currentRoot = shoulder,
            startRoot = startPose.shoulder,
            startMid = startPose.nearElbow,
            startEnd = startPose.nearWrist,
            endRoot = endPose.shoulder,
            endMid = endPose.nearElbow,
            endEnd = endPose.nearWrist,
            fraction = lift,
        )
        val farLeg = interpolateTwoBoneChain(
            currentRoot = hip,
            startRoot = startPose.hip,
            startMid = startPose.farKnee,
            startEnd = startPose.farAnkle,
            endRoot = endPose.hip,
            endMid = endPose.farKnee,
            endEnd = endPose.farAnkle,
            fraction = lift,
        )
        val nearLeg = interpolateTwoBoneChain(
            currentRoot = hip,
            startRoot = startPose.hip,
            startMid = startPose.nearKnee,
            startEnd = startPose.nearAnkle,
            endRoot = endPose.hip,
            endMid = endPose.nearKnee,
            endEnd = endPose.nearAnkle,
            fraction = lift,
        )

        val farFoot = interpolateFoot(
            currentAnkle = farLeg.second,
            startAnkle = startPose.farAnkle,
            startFoot = startPose.farFoot,
            endAnkle = endPose.farAnkle,
            endFoot = endPose.farFoot,
            fraction = lift,
        )
        val nearFoot = interpolateFoot(
            currentAnkle = nearLeg.second,
            startAnkle = startPose.nearAnkle,
            startFoot = startPose.nearFoot,
            endAnkle = endPose.nearAnkle,
            endFoot = endPose.nearFoot,
            fraction = lift,
        )

        return EvaluatedExerciseFrame(
            pose = BodyPose(
                head = head,
                shoulder = shoulder,
                spineControl = spineControl,
                hip = hip,
                nearElbow = nearArm.first,
                nearWrist = nearArm.second,
                farElbow = farArm.first,
                farWrist = farArm.second,
                nearKnee = nearLeg.first,
                nearAnkle = nearLeg.second,
                farKnee = farLeg.first,
                farAnkle = farLeg.second,
                nearFoot = nearFoot,
                farFoot = farFoot,
            ),
            activeBodyParts = activeParts,
            phase = phase,
            holdProgress = holdProgress,
            holdSpanRatio = holdSpan,
            shouldMirrorHorizontally = spec.bilateralMode == ExerciseBilateralMode.HORIZONTAL_MIRROR &&
                activeSide == ExerciseSide.RIGHT,
        )
    }

    fun computeSampledBounds(spec: ExerciseAnimationSpec): StageBoundingBox {
        var minimumX = Float.POSITIVE_INFINITY
        var minimumY = Float.POSITIVE_INFINITY
        var maximumX = Float.NEGATIVE_INFINITY
        var maximumY = Float.NEGATIVE_INFINITY

        fun include(x: Float, y: Float, radius: Float) {
            minimumX = min(minimumX, x - radius)
            maximumX = max(maximumX, x + radius)
            minimumY = min(minimumY, y - radius)
            maximumY = max(maximumY, y + radius)
        }

        val sidesToSample = if (spec.bilateralMode == ExerciseBilateralMode.OPPOSITE_LIMB_SWAP) {
            listOf(ExerciseSide.LEFT, ExerciseSide.RIGHT)
        } else {
            listOf(ExerciseSide.LEFT)
        }

        sidesToSample.forEach { side ->
            for (step in 0..40) {
                val pose = evaluateFrame(
                    spec = spec,
                    cycleProgress = step / 40f,
                    peakHoldRatio = 0.25f,
                    activeSide = side,
                ).pose
                include(pose.head.x, pose.head.y, HEAD_BOUNDING_RADIUS)
                include(pose.shoulder.x, pose.shoulder.y, LIMB_BOUNDING_RADIUS)
                include(pose.hip.x, pose.hip.y, LIMB_BOUNDING_RADIUS)
                include(pose.nearElbow.x, pose.nearElbow.y, LIMB_BOUNDING_RADIUS)
                include(pose.nearWrist.x, pose.nearWrist.y, LIMB_BOUNDING_RADIUS)
                include(pose.farElbow.x, pose.farElbow.y, LIMB_BOUNDING_RADIUS)
                include(pose.farWrist.x, pose.farWrist.y, LIMB_BOUNDING_RADIUS)
                include(pose.nearKnee.x, pose.nearKnee.y, LIMB_BOUNDING_RADIUS)
                include(pose.nearAnkle.x, pose.nearAnkle.y, LIMB_BOUNDING_RADIUS)
                include(pose.nearFoot.x, pose.nearFoot.y, 4.5f)
                include(pose.farKnee.x, pose.farKnee.y, LIMB_BOUNDING_RADIUS)
                include(pose.farAnkle.x, pose.farAnkle.y, LIMB_BOUNDING_RADIUS)
                include(pose.farFoot.x, pose.farFoot.y, 4.5f)

                for (spineStep in 1..3) {
                    val fraction = spineStep / 4f
                    val inverse = 1f - fraction
                    val curveX = inverse * inverse * pose.hip.x +
                        2f * inverse * fraction * pose.spineControl.x +
                        fraction * fraction * pose.shoulder.x
                    val curveY = inverse * inverse * pose.hip.y +
                        2f * inverse * fraction * pose.spineControl.y +
                        fraction * fraction * pose.shoulder.y
                    include(curveX, curveY, 8f)
                }
            }
        }

        when (spec.stageProp) {
            ExerciseStageProp.PERSPECTIVE_MAT,
            ExerciseStageProp.PERSPECTIVE_BALL,
            -> {
                minimumY = min(minimumY, 96f)
                maximumY = max(maximumY, 168f)
                minimumX -= 6f
                maximumX += 6f
            }

            ExerciseStageProp.ISOMETRIC_CORNER_MAT -> {
                minimumX = min(minimumX, 50f)
                maximumX = max(maximumX, 268f)
                minimumY = min(minimumY, 72f)
                maximumY = max(maximumY, 178f)
            }

            ExerciseStageProp.WALL_LEFT -> {
                minimumX = min(minimumX, 94f)
                maximumY = max(maximumY, FLOOR_MAT_BASELINE_Y + 7f)
            }

            ExerciseStageProp.BENCH_SEATED -> {
                minimumX = min(minimumX, 116f)
                maximumY = max(maximumY, FLOOR_MAT_BASELINE_Y + 7f)
            }

            ExerciseStageProp.BENCH_REAR -> {
                minimumX = min(minimumX, 74f)
                maximumY = max(maximumY, FLOOR_MAT_BASELINE_Y + 7f)
            }

            ExerciseStageProp.FLOOR_MAT,
            ExerciseStageProp.DUMBBELLS,
            -> {
                maximumY = max(maximumY, FLOOR_MAT_BASELINE_Y + 7f)
            }
        }

        return StageBoundingBox(
            left = minimumX - BOUNDING_PADDING,
            top = minimumY - BOUNDING_PADDING,
            width = (maximumX - minimumX) + BOUNDING_PADDING * 2f,
            height = (maximumY - minimumY) + BOUNDING_PADDING * 2f,
        )
    }

    fun computeUnifiedGroupDimensions(
        specs: List<ExerciseAnimationSpec>,
    ): Map<ExerciseScaleGroup, Pair<Float, Float>> {
        val grouped = specs.groupBy { it.scaleGroup }
        return grouped.mapValues { (_, groupSpecs) ->
            var maxWidth = 1f
            var maxHeight = 1f
            groupSpecs.forEach { spec ->
                val bounds = computeSampledBounds(spec)
                maxWidth = max(maxWidth, bounds.width)
                maxHeight = max(maxHeight, bounds.height)
            }
            maxWidth to maxHeight
        }
    }

    private fun interpolateFoot(
        currentAnkle: JointPoint,
        startAnkle: JointPoint,
        startFoot: JointPoint,
        endAnkle: JointPoint,
        endFoot: JointPoint,
        fraction: Float,
    ): JointPoint {
        if (distance(startFoot, endFoot) < PLANTED_DISTAL_THRESHOLD) {
            return lerpPoint(startFoot, endFoot, fraction)
        }
        return polarArcStep(
            currentParent = currentAnkle,
            startParent = startAnkle,
            startChild = startFoot,
            endParent = endAnkle,
            endChild = endFoot,
            fraction = fraction,
        )
    }

    private fun interpolateTwoBoneChain(
        currentRoot: JointPoint,
        startRoot: JointPoint,
        startMid: JointPoint,
        startEnd: JointPoint,
        endRoot: JointPoint,
        endMid: JointPoint,
        endEnd: JointPoint,
        fraction: Float,
    ): Pair<JointPoint, JointPoint> {
        if (distance(startEnd, endEnd) < PLANTED_DISTAL_THRESHOLD) {
            return lerpPoint(startMid, endMid, fraction) to lerpPoint(startEnd, endEnd, fraction)
        }
        val mid = polarArcStep(currentRoot, startRoot, startMid, endRoot, endMid, fraction)
        val end = polarArcStep(mid, startMid, startEnd, endMid, endEnd, fraction)
        return mid to end
    }

    private fun polarArcStep(
        currentParent: JointPoint,
        startParent: JointPoint,
        startChild: JointPoint,
        endParent: JointPoint,
        endChild: JointPoint,
        fraction: Float,
    ): JointPoint {
        val startAngle = atan2(
            (startChild.y - startParent.y).toDouble(),
            (startChild.x - startParent.x).toDouble(),
        )
        val endAngle = atan2(
            (endChild.y - endParent.y).toDouble(),
            (endChild.x - endParent.x).toDouble(),
        )
        var deltaAngle = endAngle - startAngle
        while (deltaAngle > PI) deltaAngle -= 2.0 * PI
        while (deltaAngle < -PI) deltaAngle += 2.0 * PI

        val radius = lerp(distance(startParent, startChild), distance(endParent, endChild), fraction)
        val angle = startAngle + deltaAngle * fraction
        return JointPoint(
            x = currentParent.x + cos(angle).toFloat() * radius,
            y = currentParent.y + sin(angle).toFloat() * radius,
        )
    }

    private fun swapBodyPart(part: ExerciseBodyPart): ExerciseBodyPart = when (part) {
        ExerciseBodyPart.NEAR_ARM -> ExerciseBodyPart.FAR_ARM
        ExerciseBodyPart.FAR_ARM -> ExerciseBodyPart.NEAR_ARM
        ExerciseBodyPart.NEAR_THIGH -> ExerciseBodyPart.FAR_THIGH
        ExerciseBodyPart.FAR_THIGH -> ExerciseBodyPart.NEAR_THIGH
        ExerciseBodyPart.NEAR_SHIN -> ExerciseBodyPart.FAR_SHIN
        ExerciseBodyPart.FAR_SHIN -> ExerciseBodyPart.NEAR_SHIN
        ExerciseBodyPart.NECK,
        ExerciseBodyPart.SPINE,
        -> part
    }

    private fun easeInOutCubic(fraction: Float): Float =
        if (fraction < 0.5f) {
            4f * fraction * fraction * fraction
        } else {
            1f - (-2f * fraction + 2f).pow(3) / 2f
        }

    private fun lerp(start: Float, end: Float, fraction: Float): Float =
        start + (end - start) * fraction

    private fun lerpPoint(start: JointPoint, end: JointPoint, fraction: Float): JointPoint =
        JointPoint(
            x = lerp(start.x, end.x, fraction),
            y = lerp(start.y, end.y, fraction),
        )

    private fun distance(first: JointPoint, second: JointPoint): Float =
        hypot(second.x - first.x, second.y - first.y)
}
