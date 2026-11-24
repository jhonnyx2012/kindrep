package com.jhonnybarrios.kindrep.ui.components

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
import androidx.compose.ui.unit.dp
import com.jhonnybarrios.kindrep.navigation.TopLevelTab
import com.jhonnybarrios.kindrep.ui.theme.LocalKindrepPalette

@Composable
fun KindrepBottomBar(
    selectedTab: TopLevelTab,
    onSelectTab: (TopLevelTab) -> Unit,
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
                TopLevelTab.entries.forEach { tab ->
                    val isSelected = tab == selectedTab
                    val pillColor = if (isSelected) palette.primary else Color.Transparent
                    val contentColor = if (isSelected) palette.onPrimary else palette.textMuted

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(pillColor)
                            .clickable { onSelectTab(tab) }
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        TabVectorIcon(
                            tab = tab,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            text = tab.label,
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
private fun TabVectorIcon(
    tab: TopLevelTab,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.095f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (tab) {
            TopLevelTab.TODAY -> {
                val house = Path().apply {
                    moveTo(w * 0.18f, h * 0.46f)
                    lineTo(w * 0.50f, h * 0.18f)
                    lineTo(w * 0.82f, h * 0.46f)
                    lineTo(w * 0.82f, h * 0.84f)
                    lineTo(w * 0.18f, h * 0.84f)
                    close()
                }
                drawPath(house, color = tint, style = stroke)
                drawLine(tint, Offset(w * 0.42f, h * 0.84f), Offset(w * 0.42f, h * 0.60f), stroke.width, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.58f, h * 0.84f), Offset(w * 0.58f, h * 0.60f), stroke.width, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.42f, h * 0.60f), Offset(w * 0.58f, h * 0.60f), stroke.width, StrokeCap.Round)
            }

            TopLevelTab.ROUTINES -> {
                val rows = listOf(0.27f, 0.50f, 0.73f)
                rows.forEach { ry ->
                    drawCircle(color = tint, radius = w * 0.055f, center = Offset(w * 0.22f, h * ry))
                    drawLine(tint, Offset(w * 0.38f, h * ry), Offset(w * 0.82f, h * ry), stroke.width, StrokeCap.Round)
                }
            }

            TopLevelTab.PROGRESS -> {
                val barW = w * 0.16f
                drawRoundRect(tint, Offset(w * 0.18f, h * 0.54f), Size(barW, h * 0.30f), CornerRadius(3f, 3f), style = stroke)
                drawRoundRect(tint, Offset(w * 0.42f, h * 0.36f), Size(barW, h * 0.48f), CornerRadius(3f, 3f), style = stroke)
                drawRoundRect(tint, Offset(w * 0.66f, h * 0.20f), Size(barW, h * 0.64f), CornerRadius(3f, 3f), style = stroke)
            }
        }
    }
}
