package com.jhonnyb.kindrep.ui.screens.checkin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import com.jhonnyb.kindrep.ui.components.KindrepBunnyMascot
import com.jhonnyb.kindrep.ui.components.MascotPose
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun PostSessionCheckInScreen(
    routineName: String,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        KindrepBunnyMascot(
            pose = MascotPose.Celebration,
            modifier = Modifier.size(width = 240.dp, height = 180.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.check_in_title),
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(Res.string.check_in_subtitle, routineName),
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onDone,
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
