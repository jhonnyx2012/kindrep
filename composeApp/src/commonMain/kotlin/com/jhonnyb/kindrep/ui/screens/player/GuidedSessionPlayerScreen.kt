package com.jhonnyb.kindrep.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.player_exit_action
import com.jhonnyb.kindrep.generated.resources.player_preview_finish_action
import com.jhonnyb.kindrep.generated.resources.player_preview_placeholder_note
import com.jhonnyb.kindrep.generated.resources.player_sample_exercise_heading
import com.jhonnyb.kindrep.generated.resources.player_sample_repetition_counter
import com.jhonnyb.kindrep.platform.LoopingExerciseVideo
import com.jhonnyb.kindrep.ui.components.ExerciseFigurePlaceholder
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun GuidedSessionPlayerScreen(
    routineIdentifier: String,
    onFinishToCheckIn: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val isInInspectionMode = LocalInspectionMode.current

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(palette.canvas)
            .safeDrawingPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1.3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp)),
        ) {
            if (isInInspectionMode) {
                ExerciseFigurePlaceholder(modifier = Modifier.fillMaxSize())
            } else {
                LoopingExerciseVideo(
                    mediaKey = routineIdentifier,
                    isPlaying = true,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.player_sample_exercise_heading),
                    style = MaterialTheme.typography.titleLarge,
                    color = palette.textPrimary,
                )
                TextButton(onClick = onExit) {
                    Text(
                        text = stringResource(Res.string.player_exit_action),
                        color = palette.textMuted,
                    )
                }
            }
            Column {
                Text(
                    text = stringResource(Res.string.player_sample_repetition_counter),
                    style = MaterialTheme.typography.displayLarge,
                    color = palette.textPrimary,
                )
                Text(
                    text = stringResource(Res.string.player_preview_placeholder_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textMuted,
                )
            }
            Button(
                onClick = onFinishToCheckIn,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_preview_finish_action),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview(widthDp = 874, heightDp = 402)
@Composable
private fun GuidedSessionPlayerScreenPreview() {
    KindrepTheme {
        GuidedSessionPlayerScreen(
            routineIdentifier = RoutineOverview.sampleLowerBackRecovery().identifier,
            onFinishToCheckIn = {},
            onExit = {},
        )
    }
}
