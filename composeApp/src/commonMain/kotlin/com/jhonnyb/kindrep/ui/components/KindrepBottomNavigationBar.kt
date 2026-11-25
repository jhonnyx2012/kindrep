package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.navigation_progress
import com.jhonnyb.kindrep.generated.resources.navigation_routines
import com.jhonnyb.kindrep.generated.resources.navigation_today
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun KindrepBottomNavigationBar(
    selectedDestination: TopLevelDestination,
    onSelectDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = palette.surface,
        tonalElevation = 0.dp,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.border)
                    .padding(top = 1.dp),
            ) {}
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TopLevelDestination.entries.forEach { destination ->
                    val isSelected = destination == selectedDestination
                    val pillColor = if (isSelected) palette.primary else Color.Transparent
                    val contentColor = if (isSelected) palette.onPrimary else palette.textMuted

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(pillColor)
                            .clickable { onSelectDestination(destination) }
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        DestinationVectorIcon(
                            destination = destination,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            text = destinationLabel(destination),
                            style = MaterialTheme.typography.labelMedium,
                            color = contentColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun destinationLabel(destination: TopLevelDestination): String = when (destination) {
    TopLevelDestination.TODAY -> stringResource(Res.string.navigation_today)
    TopLevelDestination.ROUTINES -> stringResource(Res.string.navigation_routines)
    TopLevelDestination.PROGRESS -> stringResource(Res.string.navigation_progress)
}

@Composable
private fun DestinationVectorIcon(
    destination: TopLevelDestination,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val iconWidth = size.width
        val iconHeight = size.height
        val stroke = Stroke(width = iconWidth * 0.095f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (destination) {
            TopLevelDestination.TODAY -> {
                val housePath = Path().apply {
                    moveTo(iconWidth * 0.18f, iconHeight * 0.46f)
                    lineTo(iconWidth * 0.50f, iconHeight * 0.18f)
                    lineTo(iconWidth * 0.82f, iconHeight * 0.46f)
                    lineTo(iconWidth * 0.82f, iconHeight * 0.84f)
                    lineTo(iconWidth * 0.18f, iconHeight * 0.84f)
                    close()
                }
                drawPath(housePath, color = tint, style = stroke)
                drawLine(tint, Offset(iconWidth * 0.42f, iconHeight * 0.84f), Offset(iconWidth * 0.42f, iconHeight * 0.60f), stroke.width, StrokeCap.Round)
                drawLine(tint, Offset(iconWidth * 0.58f, iconHeight * 0.84f), Offset(iconWidth * 0.58f, iconHeight * 0.60f), stroke.width, StrokeCap.Round)
                drawLine(tint, Offset(iconWidth * 0.42f, iconHeight * 0.60f), Offset(iconWidth * 0.58f, iconHeight * 0.60f), stroke.width, StrokeCap.Round)
            }

            TopLevelDestination.ROUTINES -> {
                listOf(0.27f, 0.50f, 0.73f).forEach { rowCenterY ->
                    drawCircle(color = tint, radius = iconWidth * 0.055f, center = Offset(iconWidth * 0.22f, iconHeight * rowCenterY))
                    drawLine(tint, Offset(iconWidth * 0.38f, iconHeight * rowCenterY), Offset(iconWidth * 0.82f, iconHeight * rowCenterY), stroke.width, StrokeCap.Round)
                }
            }

            TopLevelDestination.PROGRESS -> {
                val barWidth = iconWidth * 0.16f
                drawRoundRect(tint, Offset(iconWidth * 0.18f, iconHeight * 0.54f), Size(barWidth, iconHeight * 0.30f), CornerRadius(3f, 3f), style = stroke)
                drawRoundRect(tint, Offset(iconWidth * 0.42f, iconHeight * 0.36f), Size(barWidth, iconHeight * 0.48f), CornerRadius(3f, 3f), style = stroke)
                drawRoundRect(tint, Offset(iconWidth * 0.66f, iconHeight * 0.20f), Size(barWidth, iconHeight * 0.64f), CornerRadius(3f, 3f), style = stroke)
            }
        }
    }
}

@Preview
@Composable
private fun KindrepBottomNavigationBarPreview() {
    KindrepTheme {
        KindrepBottomNavigationBar(
            selectedDestination = TopLevelDestination.TODAY,
            onSelectDestination = {},
        )
    }
}
