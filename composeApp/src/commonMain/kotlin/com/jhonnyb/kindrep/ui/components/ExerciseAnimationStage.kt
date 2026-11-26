package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import com.jhonnyb.kindrep.domain.model.BodyPose
import com.jhonnyb.kindrep.domain.model.ExerciseAnimationSpec
import com.jhonnyb.kindrep.domain.model.ExerciseBodyPart
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.ExerciseStageProp
import com.jhonnyb.kindrep.domain.model.JointPoint
import com.jhonnyb.kindrep.domain.usecase.ExercisePoseEvaluator
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseMediaKeys
import com.jhonnyb.kindrep.ui.animation.drawBalancedAnatomicalFoot
import com.jhonnyb.kindrep.ui.animation.drawDumbbellAtWrist
import com.jhonnyb.kindrep.ui.animation.drawIsometricCornerFigure
import com.jhonnyb.kindrep.ui.animation.drawIsometricPullArrow
import com.jhonnyb.kindrep.ui.animation.drawStagePropBackground
import com.jhonnyb.kindrep.ui.theme.KindrepPalette
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val FOREGROUND_OUTLINE_BOOST = 1.4f
private const val HEAD_RADIUS = 11.5f
private const val SHADOW_OFFSET_X = 1.5f
private const val SHADOW_OFFSET_Y = 2.2f

private val SpineStrokeBase = Stroke(width = 14.5f, cap = StrokeCap.Round)

@Composable
fun ExerciseAnimationStage(
    mediaKey: String,
    peakHoldRatio: Float,
    activeSide: ExerciseSide,
    cycleProgressProvider: () -> Float,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val spec = remember(mediaKey) { ExerciseChoreographyCatalog.resolveSpec(mediaKey) }
    val bounds = remember(mediaKey) { ExerciseChoreographyCatalog.resolveBounds(mediaKey) }
    val groupDimensions = remember(spec.scaleGroup) {
        ExerciseChoreographyCatalog.unifiedGroupDimensions[spec.scaleGroup]
            ?: (bounds.width to bounds.height)
    }
    val spinePath = remember { Path() }
    val reusablePath = remember { Path() }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(palette.surfaceVariant),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        if (canvasWidth <= 0f || canvasHeight <= 0f) return@Canvas

        val frame = ExercisePoseEvaluator.evaluateFrame(
            spec = spec,
            cycleProgress = cycleProgressProvider(),
            peakHoldRatio = peakHoldRatio,
            activeSide = activeSide,
        )
        val uniformScale = min(
            canvasWidth / groupDimensions.first,
            canvasHeight / groupDimensions.second,
        )
        val figureCenterX = bounds.centerX
        val figureCenterY = bounds.top + bounds.height * 0.5f
        val translateX = canvasWidth * 0.5f - figureCenterX * uniformScale
        val translateY = canvasHeight * 0.5f - figureCenterY * uniformScale

        translate(left = translateX, top = translateY) {
            scale(scaleX = uniformScale, scaleY = uniformScale, pivot = Offset.Zero) {
                val mirrorPivot = Offset(figureCenterX, 0f)
                val horizontalScale = if (frame.shouldMirrorHorizontally) -1f else 1f
                scale(scaleX = horizontalScale, scaleY = 1f, pivot = mirrorPivot) {
                    drawStagePropBackground(
                        spec = spec,
                        bounds = bounds,
                        pose = frame.pose,
                        visibleLeft = -translateX / uniformScale + 8f,
                        visibleRight = (canvasWidth - translateX) / uniformScale - 8f,
                        visibleTop = -translateY / uniformScale + 4f,
                        reusablePath = reusablePath,
                        palette = palette,
                    )

                    if (spec.stageProp == ExerciseStageProp.ISOMETRIC_CORNER_MAT) {
                        translate(left = SHADOW_OFFSET_X, top = SHADOW_OFFSET_Y) {
                            drawIsometricCornerFigure(
                                pose = frame.pose,
                                activeBodyParts = frame.activeBodyParts,
                                isShadow = true,
                                outlineBoost = 0f,
                                reusablePath = reusablePath,
                                palette = palette,
                            )
                        }
                        drawIsometricCornerFigure(
                            pose = frame.pose,
                            activeBodyParts = frame.activeBodyParts,
                            isShadow = false,
                            outlineBoost = FOREGROUND_OUTLINE_BOOST,
                            reusablePath = reusablePath,
                            palette = palette,
                        )
                        drawIsometricPullArrow(
                            reusablePath = reusablePath,
                            palette = palette,
                        )
                    } else {
                        drawContactShadow(
                            spec = spec,
                            pose = frame.pose,
                            palette = palette,
                        )
                        translate(left = SHADOW_OFFSET_X, top = SHADOW_OFFSET_Y) {
                            drawBackAndTorso(
                                pose = frame.pose,
                                spinePath = spinePath,
                                footPath = reusablePath,
                                colorFor = { _, _ -> palette.figureShadow },
                            )
                            drawNearLimbs(
                                pose = frame.pose,
                                widthBoost = 0f,
                                footPath = reusablePath,
                                colorFor = { palette.figureShadow },
                            )
                        }
                        drawBackAndTorso(
                            pose = frame.pose,
                            spinePath = spinePath,
                            footPath = reusablePath,
                            colorFor = { part, isFar ->
                                when {
                                    part == null -> palette.figureBody
                                    part in frame.activeBodyParts -> palette.secondary
                                    isFar -> palette.figureBack
                                    else -> palette.figureBody
                                }
                            },
                        )
                        if (spec.stageProp == ExerciseStageProp.DUMBBELLS) {
                            drawDumbbellAtWrist(wrist = frame.pose.farWrist, isFar = true, palette = palette)
                        }
                        drawNearLimbs(
                            pose = frame.pose,
                            widthBoost = FOREGROUND_OUTLINE_BOOST,
                            footPath = reusablePath,
                            colorFor = { palette.figureOutline },
                        )
                        drawNearLimbs(
                            pose = frame.pose,
                            widthBoost = 0f,
                            footPath = reusablePath,
                            colorFor = { part ->
                                if (part in frame.activeBodyParts) palette.secondary else palette.figureBody
                            },
                        )
                        if (spec.stageProp == ExerciseStageProp.DUMBBELLS) {
                            drawDumbbellAtWrist(wrist = frame.pose.nearWrist, isFar = false, palette = palette)
                        }
                        if (spec.stageProp == ExerciseStageProp.PERSPECTIVE_MAT) {
                            drawCircle(
                                color = palette.primary,
                                radius = 3.4f,
                                center = frame.pose.nearAnkle.toOffset(),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawContactShadow(
    spec: ExerciseAnimationSpec,
    pose: BodyPose,
    palette: KindrepPalette,
) {
    val isGroundedStanding = spec.stageProp == ExerciseStageProp.WALL_LEFT ||
        spec.stageProp == ExerciseStageProp.BENCH_SEATED ||
        spec.stageProp == ExerciseStageProp.BENCH_REAR ||
        spec.stageProp == ExerciseStageProp.DUMBBELLS
    val centerX = when (spec.stageProp) {
        ExerciseStageProp.BENCH_SEATED -> 160f
        ExerciseStageProp.BENCH_REAR -> 168f
        ExerciseStageProp.WALL_LEFT -> 150f
        ExerciseStageProp.DUMBBELLS -> pose.nearAnkle.x
        ExerciseStageProp.FLOOR_MAT,
        ExerciseStageProp.PERSPECTIVE_MAT,
        ExerciseStageProp.PERSPECTIVE_BALL,
        ExerciseStageProp.ISOMETRIC_CORNER_MAT,
        -> (pose.hip.x + pose.shoulder.x) * 0.5f
    }
    val centerY = if (spec.stageProp == ExerciseStageProp.PERSPECTIVE_MAT ||
        spec.stageProp == ExerciseStageProp.PERSPECTIVE_BALL
    ) {
        max(pose.hip.y, pose.shoulder.y) + 10f
    } else {
        ExercisePoseEvaluator.FLOOR_MAT_BASELINE_Y + 1f
    }
    val radiusX = if (isGroundedStanding) {
        18f
    } else {
        max(32f, abs(pose.shoulder.x - pose.hip.x) * 0.8f + 16f)
    }
    drawOval(
        color = palette.figureShadow,
        topLeft = Offset(centerX - radiusX, centerY - 3.8f),
        size = Size(radiusX * 2f, 7.6f),
    )
}

private fun DrawScope.drawBackAndTorso(
    pose: BodyPose,
    spinePath: Path,
    footPath: Path,
    colorFor: (ExerciseBodyPart?, Boolean) -> Color,
) {
    val farShinColor = colorFor(ExerciseBodyPart.FAR_SHIN, true)
    drawSegment(pose.hip, pose.farKnee, 10.5f, colorFor(ExerciseBodyPart.FAR_THIGH, true))
    drawSegment(pose.farKnee, pose.farAnkle, 9.0f, farShinColor)
    drawBalancedAnatomicalFoot(
        ankle = pose.farAnkle,
        rawFoot = pose.farFoot,
        heelRadius = 4.0f,
        toeRadius = 2.0f,
        color = farShinColor,
        reusablePath = footPath,
    )
    drawSegment(pose.shoulder, pose.farElbow, 9.5f, colorFor(ExerciseBodyPart.FAR_ARM, true))
    drawSegment(pose.farElbow, pose.farWrist, 8.5f, colorFor(ExerciseBodyPart.FAR_ARM, true))
    drawSegment(pose.shoulder, pose.head, 8.5f, colorFor(ExerciseBodyPart.NECK, false))

    spinePath.reset()
    spinePath.moveTo(pose.hip.x, pose.hip.y)
    spinePath.quadraticTo(
        pose.spineControl.x,
        pose.spineControl.y,
        pose.shoulder.x,
        pose.shoulder.y,
    )
    drawPath(
        path = spinePath,
        color = colorFor(ExerciseBodyPart.SPINE, false),
        style = SpineStrokeBase,
    )
    drawCircle(
        color = colorFor(null, false),
        radius = HEAD_RADIUS,
        center = pose.head.toOffset(),
    )
}

private fun DrawScope.drawNearLimbs(
    pose: BodyPose,
    widthBoost: Float,
    footPath: Path,
    colorFor: (ExerciseBodyPart) -> Color,
) {
    val nearShinColor = colorFor(ExerciseBodyPart.NEAR_SHIN)
    drawSegment(pose.hip, pose.nearKnee, 11.5f + widthBoost, colorFor(ExerciseBodyPart.NEAR_THIGH))
    drawSegment(pose.nearKnee, pose.nearAnkle, 9.5f + widthBoost, nearShinColor)
    drawBalancedAnatomicalFoot(
        ankle = pose.nearAnkle,
        rawFoot = pose.nearFoot,
        heelRadius = 4.2f + widthBoost * 0.5f,
        toeRadius = 2.1f + widthBoost * 0.5f,
        color = nearShinColor,
        reusablePath = footPath,
    )
    drawSegment(pose.shoulder, pose.nearElbow, 10.0f + widthBoost, colorFor(ExerciseBodyPart.NEAR_ARM))
    drawSegment(pose.nearElbow, pose.nearWrist, 9.0f + widthBoost, colorFor(ExerciseBodyPart.NEAR_ARM))
}

private fun DrawScope.drawSegment(
    start: JointPoint,
    end: JointPoint,
    strokeWidth: Float,
    color: Color,
) {
    drawLine(
        color = color,
        start = start.toOffset(),
        end = end.toOffset(),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}

private fun JointPoint.toOffset(): Offset = Offset(x, y)

@Preview(widthDp = 420, heightDp = 240)
@Composable
private fun ExerciseAnimationStagePreview() {
    KindrepTheme {
        ExerciseAnimationStage(
            mediaKey = ExerciseMediaKeys.HAMSTRING_BRIDGE,
            peakHoldRatio = 0.30f,
            activeSide = ExerciseSide.LEFT,
            cycleProgressProvider = { 0.5f },
        )
    }
}
