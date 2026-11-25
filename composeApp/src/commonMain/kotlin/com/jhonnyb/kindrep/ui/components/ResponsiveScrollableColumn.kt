package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.app_name
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

val DefaultScreenMaxContentWidth: Dp = 640.dp
val CompactFormMaxContentWidth: Dp = 560.dp

@Composable
fun ResponsiveScrollableColumn(
    modifier: Modifier = Modifier,
    maxContentWidth: Dp = DefaultScreenMaxContentWidth,
    applySafeDrawingInsets: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val insetModifier = if (applySafeDrawingInsets) Modifier.safeDrawingPadding() else Modifier

    Column(
        modifier = modifier
            .fillMaxSize()
            .then(insetModifier)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxContentWidth)
                .fillMaxWidth()
                .padding(contentPadding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }
}

@Preview(widthDp = 874, heightDp = 402)
@Composable
private fun ResponsiveScrollableColumnPreview() {
    KindrepTheme {
        Surface(color = LocalKindrepPalette.current.canvas) {
            ResponsiveScrollableColumn(applySafeDrawingInsets = true) {
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = LocalKindrepPalette.current.textPrimary,
                )
            }
        }
    }
}
