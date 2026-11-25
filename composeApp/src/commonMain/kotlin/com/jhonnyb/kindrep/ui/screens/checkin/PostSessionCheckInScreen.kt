package com.jhonnyb.kindrep.ui.screens.checkin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.check_in_back_to_today
import com.jhonnyb.kindrep.generated.resources.check_in_subtitle
import com.jhonnyb.kindrep.generated.resources.check_in_title
import com.jhonnyb.kindrep.ui.components.CompactFormMaxContentWidth
import com.jhonnyb.kindrep.ui.components.KindrepBunnyMascot
import com.jhonnyb.kindrep.ui.components.MascotPose
import com.jhonnyb.kindrep.ui.components.ResponsiveScrollableColumn
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun PostSessionCheckInScreen(
    routineName: String,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResponsiveScrollableColumn(
        modifier = modifier,
        maxContentWidth = CompactFormMaxContentWidth,
        applySafeDrawingInsets = true,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CheckInCelebrationHeader(routineName = routineName)
        CheckInPrimaryButton(onClick = onDone)
    }
}

@Composable
private fun CheckInCelebrationHeader(routineName: String) {
    val palette = LocalKindrepPalette.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        KindrepBunnyMascot(
            pose = MascotPose.Celebration,
            modifier = Modifier.size(width = 200.dp, height = 150.dp),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.check_in_title),
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(Res.string.check_in_subtitle, routineName),
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CheckInPrimaryButton(onClick: () -> Unit) {
    val palette = LocalKindrepPalette.current

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = palette.primary,
            contentColor = palette.onPrimary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Text(
            text = stringResource(Res.string.check_in_back_to_today),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(widthDp = 402, heightDp = 874)
@Composable
private fun PostSessionCheckInScreenPreview() {
    KindrepTheme {
        Surface(color = LocalKindrepPalette.current.canvas) {
            PostSessionCheckInScreen(
                routineName = RoutineOverview.sampleLowerBackRecovery().name,
                onDone = {},
            )
        }
    }
}
