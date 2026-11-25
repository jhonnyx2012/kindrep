package com.jhonnyb.kindrep.ui.screens.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.exercise_picker_subtitle
import com.jhonnyb.kindrep.generated.resources.exercise_picker_title
import com.jhonnyb.kindrep.generated.resources.navigation_back_action
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpcomingDetailScreen(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedButton(
            onClick = onBack,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.navigation_back_action),
                color = palette.textPrimary,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
        )
    }
}

@Preview(widthDp = 402, heightDp = 874)
@Composable
private fun UpcomingDetailScreenPreview() {
    KindrepTheme {
        Surface(color = LocalKindrepPalette.current.canvas) {
            UpcomingDetailScreen(
                title = stringResource(Res.string.exercise_picker_title),
                subtitle = stringResource(Res.string.exercise_picker_subtitle),
                onBack = {},
            )
        }
    }
}
