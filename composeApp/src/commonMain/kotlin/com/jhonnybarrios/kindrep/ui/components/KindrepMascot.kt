package com.jhonnybarrios.kindrep.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import com.jhonnybarrios.kindrep.ui.theme.LocalKindrepPalette
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
    val animValue by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "poseProgress",
    )

    Canvas(modifier = modifier) {
        val sx = size.width / 150f
        val sy = size.height / 112f
        val s = min(sx, sy)
        val dx = (size.width - 150f * s) / 2f
        val dy = (size.height - 112f * s) / 2f

        translate(dx, dy) {
            scale(s, s, pivot = Offset.Zero) {
                val strokeMain = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                val strokeEye = Stroke(width = 3.3f, cap = StrokeCap.Round)

                when (pose) {
                    MascotPose.Welcome -> {
                        drawCircle(color = palette.mascotAccent, radius = 6.2f, center = Offset(33f, 86f))
                        drawBunnyEars(cx = 72f, y = 44f, spreadL = -8f, spreadR = 12f, body = palette.mascotBody, leftStroke = palette.mascotStroke, rightStroke = palette.mascotAccent)
                        rotate(degrees = -7f * animValue, pivot = Offset(102f, 86f)) {
                            val arm = Path().apply {
                                moveTo(100f, 86f)
                                cubicTo(113f, 79f, 121f, 68f, 116f, 61f)
                                cubicTo(111f, 55f, 102f, 63f, 96f, 74f)
                            }
                            drawPath(arm, color = palette.mascotBody, style = Fill)
                            drawPath(arm, color = palette.mascotStroke, style = strokeMain)
                        }
                        val body = bunnyTorsoPath(cx = 72f, topY = 41f)
                        drawPath(body, color = palette.mascotBody, style = Fill)
                        drawPath(body, color = palette.mascotStroke, style = strokeMain)
                        drawCurvedEyes(cx = 72f, cy = 61f, dip = -3.8f, color = palette.mascotEyes, stroke = strokeEye)
                        drawRoundRect(color = palette.matFill, topLeft = Offset(18f, 94f), size = Size(114f, 6f), cornerRadius = CornerRadius(3f, 3f))
                    }

                    MascotPose.TodayCompanion -> {
                        drawCircle(color = palette.mascotAccent.copy(alpha = 0.85f), radius = 3.5f, center = Offset(118f, 44f))
                        drawCircle(color = palette.mascotAccent, radius = 6.2f, center = Offset(34f, 86f))
                        drawBunnyEars(cx = 75f, y = 48f, spreadL = -6f, spreadR = 10f, body = palette.mascotBody, leftStroke = palette.mascotStroke, rightStroke = palette.mascotAccent)
                        val body = bunnyTorsoPath(cx = 75f, topY = 44f)
                        drawPath(body, color = palette.mascotBody, style = Fill)
                        drawPath(body, color = palette.mascotStroke, style = strokeMain)
                        drawCurvedEyes(cx = 75f, cy = 64f, dip = -3.2f, color = palette.mascotEyes, stroke = strokeEye)
                        drawRoundRect(color = palette.matFill, topLeft = Offset(18f, 94f), size = Size(114f, 6f), cornerRadius = CornerRadius(3f, 3f))
                        val pawStroke = Stroke(width = 4.4f, cap = StrokeCap.Round)
                        drawOval(color = palette.mascotBody, topLeft = Offset(47.5f, 88.5f), size = Size(17f, 11f))
                        drawOval(color = palette.mascotStroke, topLeft = Offset(47.5f, 88.5f), size = Size(17f, 11f), style = pawStroke)
                        drawOval(color = palette.mascotBody, topLeft = Offset(85.5f, 88.5f), size = Size(17f, 11f))
                        drawOval(color = palette.mascotStroke, topLeft = Offset(85.5f, 88.5f), size = Size(17f, 11f), style = pawStroke)
                    }

                    MascotPose.Celebration -> {
                        // Sparkles
                        val sparkStroke = Stroke(width = 3f, cap = StrokeCap.Round)
                        drawLine(palette.mascotAccent, Offset(22f, 30f), Offset(30f, 30f), strokeWidth = sparkStroke.width, cap = StrokeCap.Round)
                        drawLine(palette.mascotAccent, Offset(26f, 26f), Offset(26f, 34f), strokeWidth = sparkStroke.width, cap = StrokeCap.Round)
                        drawLine(palette.mascotAccent, Offset(120f, 28f), Offset(128f, 28f), strokeWidth = sparkStroke.width, cap = StrokeCap.Round)
                        drawLine(palette.mascotAccent, Offset(124f, 24f), Offset(124f, 32f), strokeWidth = sparkStroke.width, cap = StrokeCap.Round)
                        drawCircle(color = palette.primary.copy(alpha = 0.85f), radius = 3f, center = Offset(126f, 50f))

                        translate(left = 0f, top = -2.5f * animValue) {
                            drawCircle(color = palette.mascotAccent, radius = 6.2f, center = Offset(37f, 86f))
                            drawBunnyEars(cx = 75f, y = 45f, spreadL = -18f, spreadR = 18f, body = palette.mascotBody, leftStroke = palette.mascotStroke, rightStroke = palette.mascotAccent)
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
                            drawPath(leftArm, color = palette.mascotStroke, style = strokeMain)
                            drawPath(rightArm, color = palette.mascotBody, style = Fill)
                            drawPath(rightArm, color = palette.mascotStroke, style = strokeMain)

                            val body = bunnyTorsoPath(cx = 75f, topY = 42f)
                            drawPath(body, color = palette.mascotBody, style = Fill)
                            drawPath(body, color = palette.mascotStroke, style = strokeMain)
                            drawCurvedEyes(cx = 75f, cy = 61f, dip = -5f, color = palette.mascotEyes, stroke = strokeEye)
                        }
                        drawRoundRect(color = palette.matFill, topLeft = Offset(18f, 94f), size = Size(114f, 6f), cornerRadius = CornerRadius(3f, 3f))
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawBunnyEars(
    cx: Float,
    y: Float,
    spreadL: Float,
    spreadR: Float,
    body: androidx.compose.ui.graphics.Color,
    leftStroke: androidx.compose.ui.graphics.Color,
    rightStroke: androidx.compose.ui.graphics.Color,
) {
    val stroke = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    rotate(degrees = spreadL, pivot = Offset(cx - 8f, y)) {
        val left = Path().apply {
            moveTo(cx - 9f, y + 3f)
            cubicTo(cx - 16f, y - 8f, cx - 21f, y - 31f, cx - 12f, y - 34f)
            cubicTo(cx - 4f, y - 36f, cx - 1f, y - 15f, cx - 3f, y + 2f)
            close()
        }
        drawPath(left, color = body, style = Fill)
        drawPath(left, color = leftStroke, style = stroke)
    }
    rotate(degrees = spreadR, pivot = Offset(cx + 8f, y)) {
        val right = Path().apply {
            moveTo(cx + 9f, y + 3f)
            cubicTo(cx + 16f, y - 8f, cx + 21f, y - 31f, cx + 12f, y - 34f)
            cubicTo(cx + 4f, y - 36f, cx + 1f, y - 15f, cx + 3f, y + 2f)
            close()
        }
        drawPath(right, color = body, style = Fill)
        drawPath(right, color = rightStroke, style = stroke)
    }
}

private fun bunnyTorsoPath(cx: Float, topY: Float): Path = Path().apply {
    moveTo(cx - 34f, 96f)
    cubicTo(cx - 33f, 78f, cx - 26f, 71f, cx - 22f, 67f)
    cubicTo(cx - 25f, topY + 11f, cx - 16f, topY, cx, topY)
    cubicTo(cx + 16f, topY, cx + 25f, topY + 11f, cx + 22f, 67f)
    cubicTo(cx + 26f, 71f, cx + 33f, 78f, cx + 34f, 96f)
    close()
}

private fun DrawScope.drawCurvedEyes(
    cx: Float,
    cy: Float,
    dip: Float,
    color: androidx.compose.ui.graphics.Color,
    stroke: Stroke,
) {
    val eyes = Path().apply {
        moveTo(cx - 15f, cy + 1f)
        quadraticTo(cx - 11f, cy + dip, cx - 7f, cy + 1f)
        moveTo(cx + 7f, cy + 1f)
        quadraticTo(cx + 11f, cy + dip, cx + 15f, cy + 1f)
    }
    drawPath(eyes, color = color, style = stroke)
}
