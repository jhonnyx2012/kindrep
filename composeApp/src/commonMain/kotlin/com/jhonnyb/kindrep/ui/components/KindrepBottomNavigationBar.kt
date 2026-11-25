package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
            NavigationTopBorder(borderColor = palette.border)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                NavigationTabRow(
                    selectedDestination = selectedDestination,
                    onSelectDestination = onSelectDestination,
                    modifier = Modifier.widthIn(max = CompactFormMaxContentWidth),
                )
            }
        }
    }
}

@Composable
private fun NavigationTopBorder(borderColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(borderColor),
    )
}

@Composable
private fun NavigationTabRow(
    selectedDestination: TopLevelDestination,
    onSelectDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TopLevelDestination.entries.forEach { destination ->
            NavigationTabItemPill(
                destination = destination,
                isSelected = destination == selectedDestination,
                onSelect = { onSelectDestination(destination) },
            )
        }
    }
}

@Composable
private fun RowScope.NavigationTabItemPill(
    destination: TopLevelDestination,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    val pillBackgroundColor = if (isSelected) palette.primary else Color.Transparent
    val contentColor = if (isSelected) palette.onPrimary else palette.textMuted

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(pillBackgroundColor)
            .clickable(onClick = onSelect)
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
            text = stringResource(destination.labelResource),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

private val RoutineRowVerticalFractions = floatArrayOf(0.27f, 0.50f, 0.73f)

@Composable
private fun DestinationVectorIcon(
    destination: TopLevelDestination,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    val houseOutlinePath = remember { Path() }

    Canvas(modifier = modifier) {
        val iconWidth = size.width
        val iconHeight = size.height
        val strokeWidth = iconWidth * 0.095f
        val iconStroke = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (destination) {
            TopLevelDestination.TODAY -> {
                houseOutlinePath.reset()
                houseOutlinePath.apply {
                    moveTo(iconWidth * 0.18f, iconHeight * 0.46f)
                    lineTo(iconWidth * 0.50f, iconHeight * 0.18f)
                    lineTo(iconWidth * 0.82f, iconHeight * 0.46f)
                    lineTo(iconWidth * 0.82f, iconHeight * 0.84f)
                    lineTo(iconWidth * 0.18f, iconHeight * 0.84f)
                    close()
                }
                drawPath(houseOutlinePath, color = tint, style = iconStroke)
                drawLine(tint, Offset(iconWidth * 0.42f, iconHeight * 0.84f), Offset(iconWidth * 0.42f, iconHeight * 0.60f), strokeWidth, StrokeCap.Round)
                drawLine(tint, Offset(iconWidth * 0.58f, iconHeight * 0.84f), Offset(iconWidth * 0.58f, iconHeight * 0.60f), strokeWidth, StrokeCap.Round)
                drawLine(tint, Offset(iconWidth * 0.42f, iconHeight * 0.60f), Offset(iconWidth * 0.58f, iconHeight * 0.60f), strokeWidth, StrokeCap.Round)
            }

            TopLevelDestination.ROUTINES -> {
                RoutineRowVerticalFractions.forEach { rowCenterY ->
                    drawCircle(color = tint, radius = iconWidth * 0.055f, center = Offset(iconWidth * 0.22f, iconHeight * rowCenterY))
                    drawLine(tint, Offset(iconWidth * 0.38f, iconHeight * rowCenterY), Offset(iconWidth * 0.82f, iconHeight * rowCenterY), strokeWidth, StrokeCap.Round)
                }
            }

            TopLevelDestination.PROGRESS -> {
                val barWidth = iconWidth * 0.16f
                drawRoundRect(tint, Offset(iconWidth * 0.18f, iconHeight * 0.54f), Size(barWidth, iconHeight * 0.30f), CornerRadius(3f, 3f), style = iconStroke)
                drawRoundRect(tint, Offset(iconWidth * 0.42f, iconHeight * 0.36f), Size(barWidth, iconHeight * 0.48f), CornerRadius(3f, 3f), style = iconStroke)
                drawRoundRect(tint, Offset(iconWidth * 0.66f, iconHeight * 0.20f), Size(barWidth, iconHeight * 0.64f), CornerRadius(3f, 3f), style = iconStroke)
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
