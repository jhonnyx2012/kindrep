package com.jhonnyb.kindrep.ui.screens.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.welcome_accept_button
import com.jhonnyb.kindrep.generated.resources.welcome_disclaimer_heading
import com.jhonnyb.kindrep.generated.resources.welcome_disclaimer_point_clinician_plan
import com.jhonnyb.kindrep.generated.resources.welcome_disclaimer_point_no_prescription
import com.jhonnyb.kindrep.generated.resources.welcome_disclaimer_point_stop_on_pain
import com.jhonnyb.kindrep.generated.resources.welcome_privacy_note
import com.jhonnyb.kindrep.generated.resources.welcome_subtitle
import com.jhonnyb.kindrep.generated.resources.welcome_title
import com.jhonnyb.kindrep.ui.components.KindrepBunnyMascot
import com.jhonnyb.kindrep.ui.components.MascotPose
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WelcomeDisclaimerScreen(
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        WelcomeHeroHeader()
        Spacer(Modifier.height(28.dp))
        DisclaimerSummaryCard()
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))
        WelcomeActionFooter(onAccept = onAccept)
    }
}

@Composable
private fun WelcomeHeroHeader() {
    val palette = LocalKindrepPalette.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        KindrepBunnyMascot(
            pose = MascotPose.Welcome,
            modifier = Modifier.size(width = 240.dp, height = 180.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.welcome_title),
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun DisclaimerSummaryCard() {
    val palette = LocalKindrepPalette.current

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = palette.surface,
        border = BorderStroke(1.dp, palette.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(Res.string.welcome_disclaimer_heading),
                style = MaterialTheme.typography.labelMedium,
                color = palette.secondary,
            )
            DisclaimerBulletItem(Res.string.welcome_disclaimer_point_no_prescription)
            DisclaimerBulletItem(Res.string.welcome_disclaimer_point_clinician_plan)
            DisclaimerBulletItem(Res.string.welcome_disclaimer_point_stop_on_pain)
        }
    }
}

@Composable
private fun DisclaimerBulletItem(textResource: StringResource) {
    Text(
        text = stringResource(textResource),
        style = MaterialTheme.typography.bodyMedium,
        color = LocalKindrepPalette.current.textPrimary,
    )
}

@Composable
private fun WelcomeActionFooter(onAccept: () -> Unit) {
    val palette = LocalKindrepPalette.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onAccept,
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
                text = stringResource(Res.string.welcome_accept_button),
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(Res.string.welcome_privacy_note),
            style = MaterialTheme.typography.bodyMedium,
            color = palette.textMuted,
        )
    }
}

@Preview(widthDp = 402, heightDp = 874)
@Composable
private fun WelcomeDisclaimerScreenPreview() {
    KindrepTheme {
        Surface(color = LocalKindrepPalette.current.canvas) {
            WelcomeDisclaimerScreen(onAccept = {})
        }
    }
}
