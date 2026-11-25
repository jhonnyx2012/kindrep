package com.jhonnyb.kindrep.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.ui.theme.KindrepPalette
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import kotlin.math.min

enum class MascotPose {
    Welcome,
    TodayCompanion,
    Celebration,
}

@Composable
fun KindrepBunnyMascot(
    pose: MascotPose,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val transition = rememberInfiniteTransition(label = "mascotMotion")
    val animationProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "poseProgress",
    )

    Canvas(modifier = modifier) {
        val horizontalScale = size.width / VIEWPORT_WIDTH
        val verticalScale = size.height / VIEWPORT_HEIGHT
        val uniformScale = min(horizontalScale, verticalScale)
        val offsetX = (size.width - VIEWPORT_WIDTH * uniformScale) / 2f
        val offsetY = (size.height - VIEWPORT_HEIGHT * uniformScale) / 2f

        translate(offsetX, offsetY) {
            scale(uniformScale, uniformScale, pivot = Offset.Zero) {
                when (pose) {
                    MascotPose.Welcome -> drawWelcomePose(palette, animationProgress)
                    MascotPose.TodayCompanion -> drawTodayCompanionPose(palette)
                    MascotPose.Celebration -> drawCelebrationPose(palette, animationProgress)
                }
            }
        }
    }
}

private const val VIEWPORT_WIDTH = 150f
private const val VIEWPORT_HEIGHT = 112f
private const val TAIL_RADIUS = 6.2f

private val MainStroke = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
private val EyeStroke = Stroke(width = 3.3f, cap = StrokeCap.Round)

private fun DrawScope.drawWelcomePose(palette: KindrepPalette, animationProgress: Float) {
    drawCircle(color = palette.mascotAccent, radius = TAIL_RADIUS, center = Offset(33f, 86f))
    drawBunnyEars(centerX = 72f, baseY = 44f, leftSpread = -8f, rightSpread = 12f, palette = palette)
    rotate(degrees = -7f * animationProgress, pivot = Offset(102f, 86f)) {
        val wavingArm = Path().apply {
            moveTo(100f, 86f)
            cubicTo(113f, 79f, 121f, 68f, 116f, 61f)
            cubicTo(111f, 55f, 102f, 63f, 96f, 74f)
        }
        drawPath(wavingArm, color = palette.mascotBody, style = Fill)
        drawPath(wavingArm, color = palette.mascotStroke, style = MainStroke)
    }
    drawTorsoAndEyes(centerX = 72f, topY = 41f, eyeCenterY = 61f, eyeDip = -3.8f, palette = palette)
    drawMatLine(palette)
}

private fun DrawScope.drawTodayCompanionPose(palette: KindrepPalette) {
    drawCircle(color = palette.mascotAccent.copy(alpha = 0.85f), radius = 3.5f, center = Offset(118f, 44f))
    drawCircle(color = palette.mascotAccent, radius = TAIL_RADIUS, center = Offset(34f, 86f))
    drawBunnyEars(centerX = 75f, baseY = 48f, leftSpread = -6f, rightSpread = 10f, palette = palette)
    drawTorsoAndEyes(centerX = 75f, topY = 44f, eyeCenterY = 64f, eyeDip = -3.2f, palette = palette)
    drawMatLine(palette)

    val pawStroke = Stroke(width = 4.4f, cap = StrokeCap.Round)
    drawOval(color = palette.mascotBody, topLeft = Offset(47.5f, 88.5f), size = Size(17f, 11f))
    drawOval(color = palette.mascotStroke, topLeft = Offset(47.5f, 88.5f), size = Size(17f, 11f), style = pawStroke)
    drawOval(color = palette.mascotBody, topLeft = Offset(85.5f, 88.5f), size = Size(17f, 11f))
    drawOval(color = palette.mascotStroke, topLeft = Offset(85.5f, 88.5f), size = Size(17f, 11f), style = pawStroke)
}

private fun DrawScope.drawCelebrationPose(palette: KindrepPalette, animationProgress: Float) {
    drawSparkles(palette)
    translate(left = 0f, top = -2.5f * animationProgress) {
        drawCircle(color = palette.mascotAccent, radius = TAIL_RADIUS, center = Offset(37f, 86f))
        drawBunnyEars(centerX = 75f, baseY = 45f, leftSpread = -18f, rightSpread = 18f, palette = palette)
        val leftArm = Path().apply {
            moveTo(47f, 82f)
            cubicTo(34f, 74f, 26f, 62f, 31f, 55f)
            cubicTo(36f, 49f, 46f, 57f, 52f, 69f)
        }
        val rightArm = Path().apply {
            moveTo(103f, 82f)
            cubicTo(116f, 74f, 124f, 62f, 119f, 55f)
            cubicTo(114f, 49f, 104f, 57f, 98f, 69f)
        }
        drawPath(leftArm, color = palette.mascotBody, style = Fill)
        drawPath(leftArm, color = palette.mascotStroke, style = MainStroke)
        drawPath(rightArm, color = palette.mascotBody, style = Fill)
        drawPath(rightArm, color = palette.mascotStroke, style = MainStroke)
        drawTorsoAndEyes(centerX = 75f, topY = 42f, eyeCenterY = 61f, eyeDip = -5f, palette = palette)
    }
    drawMatLine(palette)
}

private fun DrawScope.drawSparkles(palette: KindrepPalette) {
    val sparkleWidth = 3f
    drawLine(palette.mascotAccent, Offset(22f, 30f), Offset(30f, 30f), sparkleWidth, StrokeCap.Round)
    drawLine(palette.mascotAccent, Offset(26f, 26f), Offset(26f, 34f), sparkleWidth, StrokeCap.Round)
    drawLine(palette.mascotAccent, Offset(120f, 28f), Offset(128f, 28f), sparkleWidth, StrokeCap.Round)
    drawLine(palette.mascotAccent, Offset(124f, 24f), Offset(124f, 32f), sparkleWidth, StrokeCap.Round)
    drawCircle(color = palette.primary.copy(alpha = 0.85f), radius = 3f, center = Offset(126f, 50f))
}

private fun DrawScope.drawTorsoAndEyes(
    centerX: Float,
    topY: Float,
    eyeCenterY: Float,
    eyeDip: Float,
    palette: KindrepPalette,
) {
    val bodyPath = Path().apply {
        moveTo(centerX - 34f, 96f)
        cubicTo(centerX - 33f, 78f, centerX - 26f, 71f, centerX - 22f, 67f)
        cubicTo(centerX - 25f, topY + 11f, centerX - 16f, topY, centerX, topY)
        cubicTo(centerX + 16f, topY, centerX + 25f, topY + 11f, centerX + 22f, 67f)
        cubicTo(centerX + 26f, 71f, centerX + 33f, 78f, centerX + 34f, 96f)
        close()
    }
    drawPath(bodyPath, color = palette.mascotBody, style = Fill)
    drawPath(bodyPath, color = palette.mascotStroke, style = MainStroke)

    val eyesPath = Path().apply {
        moveTo(centerX - 15f, eyeCenterY + 1f)
        quadraticTo(centerX - 11f, eyeCenterY + eyeDip, centerX - 7f, eyeCenterY + 1f)
        moveTo(centerX + 7f, eyeCenterY + 1f)
        quadraticTo(centerX + 11f, eyeCenterY + eyeDip, centerX + 15f, eyeCenterY + 1f)
    }
    drawPath(eyesPath, color = palette.mascotEyes, style = EyeStroke)
}

private fun DrawScope.drawBunnyEars(
    centerX: Float,
    baseY: Float,
    leftSpread: Float,
    rightSpread: Float,
    palette: KindrepPalette,
) {
    rotate(degrees = leftSpread, pivot = Offset(centerX - 8f, baseY)) {
        val leftEar = Path().apply {
            moveTo(centerX - 9f, baseY + 3f)
            cubicTo(centerX - 16f, baseY - 8f, centerX - 21f, baseY - 31f, centerX - 12f, baseY - 34f)
            cubicTo(centerX - 4f, baseY - 36f, centerX - 1f, baseY - 15f, centerX - 3f, baseY + 2f)
            close()
        }
        drawPath(leftEar, color = palette.mascotBody, style = Fill)
        drawPath(leftEar, color = palette.mascotStroke, style = MainStroke)
    }
    rotate(degrees = rightSpread, pivot = Offset(centerX + 8f, baseY)) {
        val rightEar = Path().apply {
            moveTo(centerX + 9f, baseY + 3f)
            cubicTo(centerX + 16f, baseY - 8f, centerX + 21f, baseY - 31f, centerX + 12f, baseY - 34f)
            cubicTo(centerX + 4f, baseY - 36f, centerX + 1f, baseY - 15f, centerX + 3f, baseY + 2f)
            close()
        }
        drawPath(rightEar, color = palette.mascotBody, style = Fill)
        drawPath(rightEar, color = palette.mascotAccent, style = MainStroke)
    }
}

private fun DrawScope.drawMatLine(palette: KindrepPalette) {
    drawRoundRect(
        color = palette.matFill,
        topLeft = Offset(18f, 94f),
        size = Size(114f, 6f),
        cornerRadius = CornerRadius(3f, 3f),
    )
}

@Preview
@Composable
private fun KindrepBunnyMascotPreview() {
    KindrepTheme {
        KindrepBunnyMascot(
            pose = MascotPose.Welcome,
            modifier = Modifier.size(width = 180.dp, height = 135.dp),
        )
    }
}
