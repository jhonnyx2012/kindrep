package com.jhonnyb.kindrep.ui.animation

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.jhonnyb.kindrep.domain.model.BodyPose
import com.jhonnyb.kindrep.domain.model.ExerciseAnimationSpec
import com.jhonnyb.kindrep.domain.model.ExerciseBodyPart
import com.jhonnyb.kindrep.domain.model.ExerciseStageProp
import com.jhonnyb.kindrep.domain.model.JointPoint
import com.jhonnyb.kindrep.domain.model.StageBoundingBox
import com.jhonnyb.kindrep.domain.usecase.ExercisePoseEvaluator
import com.jhonnyb.kindrep.ui.theme.KindrepPalette
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

internal const val STAGE_FLOOR_MAT_STROKE_WIDTH: Float = 5.5f
private const val STAGE_WALL_FACE_X: Float = 118f
private val PerspectiveMatStroke = Stroke(width = 1.4f)
private val BenchTopStroke = Stroke(width = 1.4f)
private val DumbbellOutlineStroke = Stroke(width = 1.0f)
private val BallOutlineStroke = Stroke(width = 1.1f)
private val TorsoJoinStroke = Stroke(width = 9.5f, join = StrokeJoin.Round, cap = StrokeCap.Round)
private val PullArrowStroke = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)

internal fun DrawScope.drawStagePropBackground(
    spec: ExerciseAnimationSpec,
    bounds: StageBoundingBox,
    pose: BodyPose,
    visibleLeft: Float,
    visibleRight: Float,
    visibleTop: Float,
    reusablePath: Path,
    palette: KindrepPalette,
) {
    val matBaselineY = ExercisePoseEvaluator.FLOOR_MAT_BASELINE_Y
    when (spec.stageProp) {
        ExerciseStageProp.PERSPECTIVE_MAT -> {
            drawPerspectiveMat(bounds = bounds, reusablePath = reusablePath, palette = palette)
        }

        ExerciseStageProp.PERSPECTIVE_BALL -> {
            drawPerspectiveMat(bounds = bounds, reusablePath = reusablePath, palette = palette)
            drawSageReleaseBall(hip = pose.hip, palette = palette)
        }

        ExerciseStageProp.ISOMETRIC_CORNER_MAT -> {
            reusablePath.reset()
            reusablePath.moveTo(96f, 86f)
            reusablePath.lineTo(268f, 120f)
            reusablePath.lineTo(222f, 178f)
            reusablePath.lineTo(50f, 140f)
            reusablePath.close()
            drawPath(path = reusablePath, color = palette.matFill)
        }

        ExerciseStageProp.FLOOR_MAT,
        ExerciseStageProp.DUMBBELLS,
        -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
        }

        ExerciseStageProp.WALL_LEFT -> {
            drawArchitecturalWall(
                visibleLeft = visibleLeft,
                visibleTop = visibleTop,
                matBaselineY = matBaselineY,
                palette = palette,
            )
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
        }

        ExerciseStageProp.BENCH_SEATED -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
            drawUnifiedBench(
                leftX = 118f,
                topY = 120f,
                width = 40f,
                matBaselineY = matBaselineY,
                palette = palette,
            )
        }

        ExerciseStageProp.BENCH_REAR -> {
            drawFloorMatLine(visibleLeft, visibleRight, matBaselineY, palette)
            drawUnifiedBench(
                leftX = 74f,
                topY = 118f,
                width = 40f,
                matBaselineY = matBaselineY,
                palette = palette,
            )
        }
    }
}

private fun DrawScope.drawPerspectiveMat(
    bounds: StageBoundingBox,
    reusablePath: Path,
    palette: KindrepPalette,
) {
    reusablePath.reset()
    reusablePath.moveTo(bounds.left + 22f, 98f)
    reusablePath.lineTo(bounds.left + bounds.width - 4f, 98f)
    reusablePath.lineTo(bounds.left + bounds.width, 166f)
    reusablePath.lineTo(bounds.left, 166f)
    reusablePath.close()
    drawPath(path = reusablePath, color = palette.matFill)
    drawPath(path = reusablePath, color = palette.matFill, style = PerspectiveMatStroke)
}

private fun DrawScope.drawArchitecturalWall(
    visibleLeft: Float,
    visibleTop: Float,
    matBaselineY: Float,
    palette: KindrepPalette,
) {
    val wallFill = if (palette.isDark) {
        palette.surface.copy(alpha = 0.78f)
    } else {
        palette.surface.copy(alpha = 0.92f)
    }
    val wallLeft = visibleLeft.coerceAtMost(STAGE_WALL_FACE_X - 42f)
    val wallWidth = (STAGE_WALL_FACE_X - wallLeft).coerceAtLeast(36f)
    val wallHeight = (matBaselineY - visibleTop).coerceAtLeast(40f)

    drawRect(
        color = wallFill,
        topLeft = Offset(wallLeft, visibleTop),
        size = Size(wallWidth, wallHeight),
    )
    drawRect(
        color = palette.matFill,
        topLeft = Offset(wallLeft, matBaselineY - 8f),
        size = Size(wallWidth, 8f),
    )
    drawLine(
        color = palette.matFill,
        start = Offset(STAGE_WALL_FACE_X, visibleTop),
        end = Offset(STAGE_WALL_FACE_X, matBaselineY),
        strokeWidth = 3.5f,
        cap = StrokeCap.Round,
    )
}

private fun DrawScope.drawSageReleaseBall(
    hip: JointPoint,
    palette: KindrepPalette,
) {
    val center = Offset(hip.x + 1f, hip.y + 8.5f)
    drawCircle(color = palette.primary, radius = 7.5f, center = center)
    drawCircle(color = palette.figureOutline, radius = 7.5f, center = center, style = BallOutlineStroke)
    drawCircle(
        color = palette.canvas.copy(alpha = 0.42f),
        radius = 1.8f,
        center = Offset(center.x - 2.2f, center.y - 2.2f),
    )
}

internal fun DrawScope.drawFloorMatLine(
    leftX: Float,
    rightX: Float,
    matBaselineY: Float,
    palette: KindrepPalette,
) {
    drawLine(
        color = palette.matFill,
        start = Offset(leftX, matBaselineY + 3f),
        end = Offset(rightX, matBaselineY + 3f),
        strokeWidth = STAGE_FLOOR_MAT_STROKE_WIDTH,
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

internal fun DrawScope.drawDumbbellAtWrist(
    wrist: JointPoint,
    isFar: Boolean,
    palette: KindrepPalette,
) {
    val centerY = wrist.y + 2f
    val plateWidth = if (isFar) 4.6f else 5.4f
    val plateHeight = if (isFar) 11.2f else 12.8f
    val barSpan = if (isFar) 7.2f else 8.2f
    val plateFill = if (palette.isDark) palette.canvas else palette.textPrimary.copy(alpha = 0.80f)
    val barFill = palette.figureOutline
    val plateBorder = if (palette.isDark) palette.figureBack else palette.textPrimary

    drawLine(
        color = barFill,
        start = Offset(wrist.x - barSpan, centerY),
        end = Offset(wrist.x + barSpan, centerY),
        strokeWidth = 3.2f,
        cap = StrokeCap.Round,
    )
    for (sign in intArrayOf(-1, 1)) {
        val left = wrist.x + sign * barSpan - plateWidth * 0.5f
        val top = centerY - plateHeight * 0.5f
        drawRoundRect(
            color = plateFill,
            topLeft = Offset(left, top),
            size = Size(plateWidth, plateHeight),
            cornerRadius = CornerRadius(2.2f, 2.2f),
        )
        drawRoundRect(
            color = plateBorder,
            topLeft = Offset(left, top),
            size = Size(plateWidth, plateHeight),
            cornerRadius = CornerRadius(2.2f, 2.2f),
            style = DumbbellOutlineStroke,
        )
    }
}

internal fun DrawScope.drawBalancedAnatomicalFoot(
    ankle: JointPoint,
    rawFoot: JointPoint,
    heelRadius: Float,
    toeRadius: Float,
    color: Color,
    reusablePath: Path,
) {
    val rawDx = rawFoot.x - ankle.x
    val rawDy = rawFoot.y - ankle.y
    val rawDistance = hypot(rawDx, rawDy)
    if (rawDistance < 0.001f) {
        drawCircle(color = color, radius = heelRadius, center = Offset(ankle.x, ankle.y))
        return
    }
    val footLength = rawDistance.coerceAtMost(13.5f)
    val footX = ankle.x + (rawDx / rawDistance) * footLength
    val footY = ankle.y + (rawDy / rawDistance) * footLength

    val angle = atan2(rawDy.toDouble(), rawDx.toDouble())
    val cosAlpha = ((heelRadius - toeRadius) / footLength).coerceIn(-0.99f, 0.99f).toDouble()
    val alpha = acos(cosAlpha)

    val topAngle = angle + alpha
    val bottomAngle = angle - alpha

    val p1x = ankle.x + (cos(topAngle) * heelRadius).toFloat()
    val p1y = ankle.y + (sin(topAngle) * heelRadius).toFloat()
    val p2x = footX + (cos(topAngle) * toeRadius).toFloat()
    val p2y = footY + (sin(topAngle) * toeRadius).toFloat()
    val p3x = footX + (cos(bottomAngle) * toeRadius).toFloat()
    val p3y = footY + (sin(bottomAngle) * toeRadius).toFloat()
    val p4x = ankle.x + (cos(bottomAngle) * heelRadius).toFloat()
    val p4y = ankle.y + (sin(bottomAngle) * heelRadius).toFloat()

    reusablePath.reset()
    reusablePath.moveTo(p1x, p1y)
    reusablePath.lineTo(p2x, p2y)
    reusablePath.lineTo(p3x, p3y)
    reusablePath.lineTo(p4x, p4y)
    reusablePath.close()

    drawPath(path = reusablePath, color = color)
    drawCircle(color = color, radius = heelRadius, center = Offset(ankle.x, ankle.y))
    drawCircle(color = color, radius = toeRadius, center = Offset(footX, footY))
}

internal fun DrawScope.drawIsometricCornerFigure(
    pose: BodyPose,
    activeBodyParts: Set<ExerciseBodyPart>,
    isShadow: Boolean,
    outlineBoost: Float,
    reusablePath: Path,
    palette: KindrepPalette,
) {
    val bodyColor = if (isShadow) palette.figureShadow else palette.figureBody
    val backColor = if (isShadow) palette.figureShadow else palette.figureBack
    val activeColor = if (isShadow) palette.figureShadow else palette.secondary
    val farShoulder = JointPoint(pose.shoulder.x + 7f, pose.shoulder.y - 11f)
    val nearShoulder = JointPoint(pose.shoulder.x - 7f, pose.shoulder.y + 11f)
    val farHip = JointPoint(pose.hip.x + 6f, pose.hip.y - 10f)
    val nearHip = JointPoint(pose.hip.x - 6f, pose.hip.y + 10f)

    // Straight resting far leg
    drawLineSegment(farHip, pose.farKnee, 10.5f, backColor)
    drawLineSegment(pose.farKnee, pose.farAnkle, 9.0f, backColor)
    drawBalancedAnatomicalFoot(pose.farAnkle, pose.farFoot, 3.8f, 1.9f, backColor, reusablePath)

    // Neck + Separated-Shoulder Quad Torso + Head
    drawLineSegment(pose.shoulder, pose.head, 8.5f, bodyColor)
    reusablePath.reset()
    reusablePath.moveTo(farShoulder.x, farShoulder.y)
    reusablePath.lineTo(nearShoulder.x, nearShoulder.y)
    reusablePath.lineTo(nearHip.x, nearHip.y)
    reusablePath.lineTo(farHip.x, farHip.y)
    reusablePath.close()
    drawPath(path = reusablePath, color = bodyColor)
    drawPath(path = reusablePath, color = bodyColor, style = TorsoJoinStroke)
    drawCircle(color = bodyColor, radius = 11.2f, center = Offset(pose.head.x, pose.head.y))

    // Resting near arm in foreground bright tone (swapped for clear left/right distinction)
    if (!isShadow) {
        drawLineSegment(nearShoulder, pose.nearElbow, 9.5f + outlineBoost, palette.figureOutline)
        drawLineSegment(pose.nearElbow, pose.nearWrist, 8.5f + outlineBoost, palette.figureOutline)
    }
    drawLineSegment(nearShoulder, pose.nearElbow, 9.5f, bodyColor)
    drawLineSegment(pose.nearElbow, pose.nearWrist, 8.5f, bodyColor)

    // Projected soft shadow onto torso/mat below the lifted working leg
    if (!isShadow) {
        val projectedKnee = JointPoint(pose.nearKnee.x + 2f, pose.nearKnee.y + 26f)
        val projectedAnkle = JointPoint(pose.nearAnkle.x + 2f, pose.nearAnkle.y + 22f)
        drawLineSegment(nearHip, projectedKnee, 13.0f, palette.figureShadow.copy(alpha = 0.24f))
        drawLineSegment(projectedKnee, projectedAnkle, 11.0f, palette.figureShadow.copy(alpha = 0.24f))
    }

    // Lifted working leg crossing over the torso
    val workingLegColor = if (ExerciseBodyPart.NEAR_THIGH in activeBodyParts) activeColor else bodyColor
    if (!isShadow) {
        drawLineSegment(nearHip, pose.nearKnee, 11.5f + outlineBoost, palette.figureOutline)
        drawLineSegment(pose.nearKnee, pose.nearAnkle, 9.5f + outlineBoost, palette.figureOutline)
        drawBalancedAnatomicalFoot(
            ankle = pose.nearAnkle,
            rawFoot = pose.nearFoot,
            heelRadius = 4.0f + outlineBoost * 0.5f,
            toeRadius = 2.0f + outlineBoost * 0.5f,
            color = palette.figureOutline,
            reusablePath = reusablePath,
        )
    }
    drawLineSegment(nearHip, pose.nearKnee, 11.5f, workingLegColor)
    drawLineSegment(pose.nearKnee, pose.nearAnkle, 9.5f, workingLegColor)
    drawBalancedAnatomicalFoot(pose.nearAnkle, pose.nearFoot, 4.0f, 2.0f, workingLegColor, reusablePath)

    // Opposite (far) pulling arm in muted tone gripping the knee
    if (!isShadow) {
        drawLineSegment(farShoulder, pose.farElbow, 9.5f + outlineBoost, palette.figureOutline)
        drawLineSegment(pose.farElbow, pose.farWrist, 8.5f + outlineBoost, palette.figureOutline)
    }
    drawLineSegment(farShoulder, pose.farElbow, 9.5f, backColor)
    drawLineSegment(pose.farElbow, pose.farWrist, 8.5f, backColor)
}

internal fun DrawScope.drawIsometricPullArrow(
    reusablePath: Path,
    palette: KindrepPalette,
) {
    val startX = 144f
    val startY = 84f
    val controlX = 166f
    val controlY = 74f
    val endX = 186f
    val endY = 82f
    val arrowColor = palette.primary.copy(alpha = 0.78f)

    reusablePath.reset()
    reusablePath.moveTo(startX, startY)
    reusablePath.quadraticTo(controlX, controlY, endX, endY)
    drawPath(path = reusablePath, color = arrowColor, style = PullArrowStroke)

    val angle = atan2((endY - controlY).toDouble(), (endX - controlX).toDouble())
    val headLength = 6.2
    reusablePath.reset()
    reusablePath.moveTo(
        endX - (headLength * cos(angle - 0.52)).toFloat(),
        endY - (headLength * sin(angle - 0.52)).toFloat(),
    )
    reusablePath.lineTo(endX, endY)
    reusablePath.lineTo(
        endX - (headLength * cos(angle + 0.52)).toFloat(),
        endY - (headLength * sin(angle + 0.52)).toFloat(),
    )
    drawPath(path = reusablePath, color = arrowColor, style = PullArrowStroke)
}

private fun DrawScope.drawLineSegment(
    start: JointPoint,
    end: JointPoint,
    strokeWidth: Float,
    color: Color,
) {
    drawLine(
        color = color,
        start = Offset(start.x, start.y),
        end = Offset(end.x, end.y),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}
