package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
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
import com.jhonnyb.kindrep.domain.model.ExerciseScaleGroup
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.ExerciseStageProp
import com.jhonnyb.kindrep.domain.model.JointPoint
import com.jhonnyb.kindrep.domain.model.StageBoundingBox
import com.jhonnyb.kindrep.domain.usecase.ExercisePoseEvaluator
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseMediaKeys
import com.jhonnyb.kindrep.ui.theme.KindrepPalette
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val FOREGROUND_OUTLINE_BOOST = 1.15f
private const val HEAD_RADIUS = 11.5f
private const val FLOOR_MAT_STROKE_WIDTH = 5.5f
private const val SHADOW_OFFSET_X = 1.5f
private const val SHADOW_OFFSET_Y = 2.2f

private val PerspectiveMatStroke = Stroke(width = 1.4f)
private val BenchTopStroke = Stroke(width = 1.4f)
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
    val perspectiveMatPath = remember { Path() }

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
                    drawStageProp(
                        spec = spec,
                        bounds = bounds,
                        visibleLeft = -translateX / uniformScale + 8f,
                        visibleRight = (canvasWidth - translateX) / uniformScale - 8f,
                        visibleTop = -translateY / uniformScale + 4f,
                        perspectiveMatPath = perspectiveMatPath,
                        palette = palette,
                    )
                    drawContactShadow(
                        spec = spec,
                        pose = frame.pose,
                        palette = palette,
                    )
                    translate(left = SHADOW_OFFSET_X, top = SHADOW_OFFSET_Y) {
                        drawBackAndTorso(
                            pose = frame.pose,
                            spinePath = spinePath,
                            colorFor = { _, _ -> palette.figureShadow },
                        )
                        drawNearLimbs(
                            pose = frame.pose,
                            widthBoost = 0f,
                            colorFor = { palette.figureShadow },
                        )
                    }
                    drawBackAndTorso(
                        pose = frame.pose,
                        spinePath = spinePath,
                        colorFor = { part, isFar ->
                            when {
                                part == null -> palette.figureBody
                                part in frame.activeBodyParts -> palette.secondary
                                isFar -> palette.figureBack
                                else -> palette.figureBody
                            }
                        },
                    )
                    drawNearLimbs(
                        pose = frame.pose,
                        widthBoost = FOREGROUND_OUTLINE_BOOST,
                        colorFor = { palette.figureOutline },
                    )
                    drawNearLimbs(
                        pose = frame.pose,
                        widthBoost = 0f,
                        colorFor = { part ->
                            if (part in frame.activeBodyParts) palette.secondary else palette.figureBody
                        },
                    )
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

private fun DrawScope.drawStageProp(
    spec: ExerciseAnimationSpec,
    bounds: StageBoundingBox,
    visibleLeft: Float,
    visibleRight: Float,
    visibleTop: Float,
    perspectiveMatPath: Path,
    palette: KindrepPalette,
) {
    val matBaselineY = ExercisePoseEvaluator.FLOOR_MAT_BASELINE_Y
    when (spec.stageProp) {
        ExerciseStageProp.PERSPECTIVE_MAT -> {
            perspectiveMatPath.reset()
            perspectiveMatPath.moveTo(bounds.left + 22f, 98f)
            perspectiveMatPath.lineTo(bounds.left + bounds.width - 4f, 98f)
            perspectiveMatPath.lineTo(bounds.left + bounds.width, 166f)
            perspectiveMatPath.lineTo(bounds.left, 166f)
            perspectiveMatPath.close()
            drawPath(path = perspectiveMatPath, color = palette.matFill)
            drawPath(path = perspectiveMatPath, color = palette.matFill, style = PerspectiveMatStroke)
        }

        ExerciseStageProp.FLOOR_MAT -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
        }

        ExerciseStageProp.WALL_LEFT -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
            drawLine(
                color = palette.matFill,
                start = Offset(124f, visibleTop),
                end = Offset(124f, matBaselineY),
                strokeWidth = 5f,
                cap = StrokeCap.Round,
            )
        }

        ExerciseStageProp.BENCH_SEATED -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
            drawUnifiedBench(leftX = 118f, topY = 120f, width = 40f, matBaselineY = matBaselineY, palette = palette)
        }

        ExerciseStageProp.BENCH_REAR -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
            drawUnifiedBench(leftX = 74f, topY = 118f, width = 40f, matBaselineY = matBaselineY, palette = palette)
        }
    }
}

private fun DrawScope.drawFloorMatLine(
    leftX: Float,
    rightX: Float,
    matBaselineY: Float,
    palette: KindrepPalette,
) {
    drawLine(
        color = palette.matFill,
        start = Offset(leftX, matBaselineY + 3f),
        end = Offset(rightX, matBaselineY + 3f),
        strokeWidth = FLOOR_MAT_STROKE_WIDTH,
        cap = StrokeCap.Round,
    )
}

private fun DrawScope.drawUnifiedBench(
    leftX: Float,
    topY: Float,
    width: Float,
    matBaselineY: Float,
    palette: KindrepPalette,
) {
    val seatHeight = 10f
    drawRoundRect(
        color = palette.matFill,
        topLeft = Offset(leftX, topY),
        size = Size(width, seatHeight),
        cornerRadius = CornerRadius(4f, 4f),
    )
    drawRoundRect(
        color = palette.matFill,
        topLeft = Offset(leftX, topY),
        size = Size(width, seatHeight),
        cornerRadius = CornerRadius(4f, 4f),
        style = BenchTopStroke,
    )
    val legTopY = topY + seatHeight
    drawLine(
        color = palette.matFill,
        start = Offset(leftX + 8f, legTopY),
        end = Offset(leftX + 8f, matBaselineY),
        strokeWidth = 4.5f,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = palette.matFill,
        start = Offset(leftX + width - 8f, legTopY),
        end = Offset(leftX + width - 8f, matBaselineY),
        strokeWidth = 4.5f,
        cap = StrokeCap.Round,
    )
}

private fun DrawScope.drawContactShadow(
    spec: ExerciseAnimationSpec,
    pose: BodyPose,
    palette: KindrepPalette,
) {
    val isGrounded = spec.stageProp == ExerciseStageProp.WALL_LEFT ||
        spec.stageProp == ExerciseStageProp.BENCH_SEATED ||
        spec.stageProp == ExerciseStageProp.BENCH_REAR
    val centerX = when (spec.stageProp) {
        ExerciseStageProp.BENCH_SEATED -> 160f
        ExerciseStageProp.BENCH_REAR -> 168f
        ExerciseStageProp.WALL_LEFT -> 143f
        ExerciseStageProp.FLOOR_MAT,
        ExerciseStageProp.PERSPECTIVE_MAT,
        -> (pose.hip.x + pose.shoulder.x) * 0.5f
    }
    val centerY = if (spec.stageProp == ExerciseStageProp.PERSPECTIVE_MAT) {
        max(pose.hip.y, pose.shoulder.y) + 10f
    } else {
        ExercisePoseEvaluator.FLOOR_MAT_BASELINE_Y + 1f
    }
    val radiusX = if (isGrounded) {
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
    colorFor: (ExerciseBodyPart?, Boolean) -> Color,
) {
    drawSegment(pose.hip, pose.farKnee, 10.5f, colorFor(ExerciseBodyPart.FAR_THIGH, true))
    drawSegment(pose.farKnee, pose.farAnkle, 9.0f, colorFor(ExerciseBodyPart.FAR_SHIN, true))
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
    colorFor: (ExerciseBodyPart) -> Color,
) {
    drawSegment(pose.hip, pose.nearKnee, 11.5f + widthBoost, colorFor(ExerciseBodyPart.NEAR_THIGH))
    drawSegment(pose.nearKnee, pose.nearAnkle, 9.5f + widthBoost, colorFor(ExerciseBodyPart.NEAR_SHIN))
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
            mediaKey = ExerciseMediaKeys.GLUTE_BRIDGE,
            peakHoldRatio = 0.30f,
            activeSide = ExerciseSide.LEFT,
            cycleProgressProvider = { 0.5f },
        )
    }
}
