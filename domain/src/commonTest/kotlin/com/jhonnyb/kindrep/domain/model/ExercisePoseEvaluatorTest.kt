package com.jhonnyb.kindrep.domain.model

import com.jhonnyb.kindrep.domain.usecase.ExercisePoseEvaluator
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExercisePoseEvaluatorTest {

    private val quadrupedBase = BodyPose(
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

    private val birdDogSpec = ExerciseAnimationSpec(
        mediaKey = "bird_dog",
        scaleGroup = ExerciseScaleGroup.FLOOR,
        bilateralMode = ExerciseBilateralMode.OPPOSITE_LIMB_SWAP,
        stageProp = ExerciseStageProp.FLOOR_MAT,
        breathTarget = ExerciseBreathTarget.NONE,
        restPose = quadrupedBase,
        peakPose = quadrupedBase.copy(
            nearElbow = JointPoint(222f, 103f),
            nearWrist = JointPoint(247f, 103f),
            farKnee = JointPoint(80f, 104f),
            farAnkle = JointPoint(48f, 104f),
        ),
        activeBodyParts = setOf(
            ExerciseBodyPart.NEAR_ARM,
            ExerciseBodyPart.FAR_THIGH,
            ExerciseBodyPart.FAR_SHIN,
        ),
    )

    @Test
    fun closedChainSupportLimbRemainsPlantedWhileOpenChainLimbPreservesLengthDuringSwing() {
        val midSwing = ExercisePoseEvaluator.evaluateFrame(
            spec = birdDogSpec,
            cycleProgress = 0.2f,
            peakHoldRatio = 0.35f,
            activeSide = ExerciseSide.LEFT,
        )

        // Planted support wrist stays anchored at rest coordinates
        assertEquals(quadrupedBase.farWrist.x, midSwing.pose.farWrist.x, 0.01f)
        assertEquals(quadrupedBase.farWrist.y, midSwing.pose.farWrist.y, 0.01f)

        // Open-chain swinging upper arm preserves its exact length mid-arc
        val restArmLength = hypot(
            quadrupedBase.nearElbow.x - quadrupedBase.shoulder.x,
            quadrupedBase.nearElbow.y - quadrupedBase.shoulder.y,
        )
        val midArmLength = hypot(
            midSwing.pose.nearElbow.x - midSwing.pose.shoulder.x,
            midSwing.pose.nearElbow.y - midSwing.pose.shoulder.y,
        )
        assertTrue(abs(restArmLength - midArmLength) < 0.2f)
        assertEquals(ExercisePhase.EXTEND, midSwing.phase)
    }

    @Test
    fun oppositeLimbSwapMirrorsActivePartsAndLimbMotionForRightSide() {
        val rightSidePeak = ExercisePoseEvaluator.evaluateFrame(
            spec = birdDogSpec,
            cycleProgress = 0.5f,
            peakHoldRatio = 0.35f,
            activeSide = ExerciseSide.RIGHT,
        )

        assertEquals(ExercisePhase.HOLD, rightSidePeak.phase)
        assertFalse(rightSidePeak.shouldMirrorHorizontally)
        assertEquals(
            setOf(
                ExerciseBodyPart.FAR_ARM,
                ExerciseBodyPart.NEAR_THIGH,
                ExerciseBodyPart.NEAR_SHIN,
            ),
            rightSidePeak.activeBodyParts,
        )
        assertEquals(247f, rightSidePeak.pose.farWrist.x, 0.5f)
        assertEquals(quadrupedBase.farWrist.x, rightSidePeak.pose.nearWrist.x, 0.5f)
    }

    @Test
    fun horizontalMirrorModeSetsMirrorFlagOnlyOnRightSide() {
        val clamshellSpec = birdDogSpec.copy(
            mediaKey = "clamshell",
            bilateralMode = ExerciseBilateralMode.HORIZONTAL_MIRROR,
            stageProp = ExerciseStageProp.PERSPECTIVE_MAT,
        )

        val leftFrame = ExercisePoseEvaluator.evaluateFrame(
            spec = clamshellSpec,
            cycleProgress = 0.5f,
            peakHoldRatio = 0.35f,
            activeSide = ExerciseSide.LEFT,
        )
        val rightFrame = ExercisePoseEvaluator.evaluateFrame(
            spec = clamshellSpec,
            cycleProgress = 0.5f,
            peakHoldRatio = 0.35f,
            activeSide = ExerciseSide.RIGHT,
        )

        assertFalse(leftFrame.shouldMirrorHorizontally)
        assertTrue(rightFrame.shouldMirrorHorizontally)
    }

    @Test
    fun unifiedGroupDimensionsShareEnvelopeAcrossFloorExercises() {
        val compactFloorSpec = birdDogSpec.copy(
            mediaKey = "glute_bridge",
            peakPose = quadrupedBase,
        )
        val groupDimensions = ExercisePoseEvaluator.computeUnifiedGroupDimensions(
            listOf(birdDogSpec, compactFloorSpec),
        )
        val floorEnvelope = groupDimensions[ExerciseScaleGroup.FLOOR]
        val birdDogBounds = ExercisePoseEvaluator.computeSampledBounds(birdDogSpec)

        assertTrue(floorEnvelope != null)
        assertEquals(birdDogBounds.width, floorEnvelope.first, 0.01f)
        assertEquals(birdDogBounds.height, floorEnvelope.second, 0.01f)
    }

    @Test
    fun lockNeutralNeckKeepsHeadInlineWithSpineAcrossHipHinge() {
        val restStanding = quadrupedBase.copy(
            head = JointPoint(154f, 32f),
            shoulder = JointPoint(154f, 54f),
            spineControl = JointPoint(154f, 74f),
            hip = JointPoint(154f, 94f),
        )
        val peakHinge = restStanding.copy(
            head = JointPoint(202f, 72f),
            shoulder = JointPoint(182f, 82f),
            spineControl = JointPoint(160f, 91f),
            hip = JointPoint(138f, 98f),
        )
        val hingeSpec = birdDogSpec.copy(
            mediaKey = "deadlift_hip_hinge",
            scaleGroup = ExerciseScaleGroup.VERTICAL,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.DUMBBELLS,
            restPose = restStanding,
            peakPose = peakHinge,
            lockNeutralNeck = true,
        )

        val midHinge = ExercisePoseEvaluator.evaluateFrame(
            spec = hingeSpec,
            cycleProgress = 0.2f,
            peakHoldRatio = 0.30f,
            activeSide = ExerciseSide.LEFT,
        ).pose

        val spineDx = midHinge.shoulder.x - midHinge.hip.x
        val spineDy = midHinge.shoulder.y - midHinge.hip.y
        val neckDx = midHinge.head.x - midHinge.shoulder.x
        val neckDy = midHinge.head.y - midHinge.shoulder.y
        val crossProduct = abs(spineDx * neckDy - spineDy * neckDx)
        assertTrue(crossProduct < 0.1f)
        assertEquals(22f, hypot(neckDx, neckDy), 0.1f)
    }

    @Test
    fun plantedFootRemainsAnchoredWhileHeelPivotsUpward() {
        val restRaise = quadrupedBase.copy(
            nearAnkle = JointPoint(158f, 153.5f),
            nearFoot = JointPoint(147.5f, 155.5f),
        )
        val peakRaise = restRaise.copy(
            nearAnkle = JointPoint(154f, 143.5f),
            nearFoot = JointPoint(147.5f, 155.5f),
        )
        val raiseSpec = birdDogSpec.copy(
            mediaKey = "soleus_heel_raise",
            scaleGroup = ExerciseScaleGroup.VERTICAL,
            bilateralMode = ExerciseBilateralMode.NONE,
            stageProp = ExerciseStageProp.WALL_LEFT,
            restPose = restRaise,
            peakPose = peakRaise,
        )

        val midRaise = ExercisePoseEvaluator.evaluateFrame(
            spec = raiseSpec,
            cycleProgress = 0.2f,
            peakHoldRatio = 0.30f,
            activeSide = ExerciseSide.LEFT,
        ).pose

        assertEquals(147.5f, midRaise.nearFoot.x, 0.01f)
        assertEquals(155.5f, midRaise.nearFoot.y, 0.01f)
        assertTrue(midRaise.nearAnkle.y < restRaise.nearAnkle.y)
    }
}
