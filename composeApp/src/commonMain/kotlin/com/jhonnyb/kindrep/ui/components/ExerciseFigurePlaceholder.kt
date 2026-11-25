package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette

@Composable
fun ExerciseFigurePlaceholder(
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(width = 180.dp, height = 120.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawLine(
                color = palette.matFill,
                start = Offset(canvasWidth * 0.08f, canvasHeight * 0.84f),
                end = Offset(canvasWidth * 0.92f, canvasHeight * 0.84f),
                strokeWidth = 10f,
                cap = StrokeCap.Round,
            )
            drawCircle(
                color = palette.figureBody,
                radius = 14f,
                center = Offset(canvasWidth * 0.66f, canvasHeight * 0.40f),
            )
            drawLine(
                color = palette.figureBody,
                start = Offset(canvasWidth * 0.38f, canvasHeight * 0.48f),
                end = Offset(canvasWidth * 0.60f, canvasHeight * 0.46f),
                strokeWidth = 16f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = palette.figureBody,
                start = Offset(canvasWidth * 0.40f, canvasHeight * 0.48f),
                end = Offset(canvasWidth * 0.38f, canvasHeight * 0.80f),
                strokeWidth = 12f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = palette.figureBody,
                start = Offset(canvasWidth * 0.58f, canvasHeight * 0.48f),
                end = Offset(canvasWidth * 0.60f, canvasHeight * 0.80f),
                strokeWidth = 12f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = palette.secondary,
                start = Offset(canvasWidth * 0.60f, canvasHeight * 0.45f),
                end = Offset(canvasWidth * 0.86f, canvasHeight * 0.38f),
                strokeWidth = 11f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = palette.secondary,
                start = Offset(canvasWidth * 0.38f, canvasHeight * 0.48f),
                end = Offset(canvasWidth * 0.14f, canvasHeight * 0.42f),
                strokeWidth = 11f,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Preview(widthDp = 320, heightDp = 180)
@Composable
private fun ExerciseFigurePlaceholderPreview() {
    KindrepTheme {
        ExerciseFigurePlaceholder()
    }
}
