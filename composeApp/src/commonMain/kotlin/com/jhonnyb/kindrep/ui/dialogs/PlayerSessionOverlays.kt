package com.jhonnyb.kindrep.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.player_break_meta_format
import com.jhonnyb.kindrep.generated.resources.player_break_meta_single_set_format
import com.jhonnyb.kindrep.generated.resources.player_exit_action
import com.jhonnyb.kindrep.generated.resources.player_info_breathing_label
import com.jhonnyb.kindrep.generated.resources.player_info_movement_label
import com.jhonnyb.kindrep.generated.resources.player_info_paused_badge
import com.jhonnyb.kindrep.generated.resources.player_info_resume_action
import com.jhonnyb.kindrep.generated.resources.player_info_safety_body
import com.jhonnyb.kindrep.generated.resources.player_info_safety_label
import com.jhonnyb.kindrep.generated.resources.player_left_side
import com.jhonnyb.kindrep.generated.resources.player_right_side
import com.jhonnyb.kindrep.generated.resources.player_switch_sides_badge
import com.jhonnyb.kindrep.generated.resources.player_switch_sides_countdown
import com.jhonnyb.kindrep.generated.resources.player_switch_sides_heading
import com.jhonnyb.kindrep.generated.resources.player_switch_sides_start_action
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerState
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseCopyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseCopyResources
import com.jhonnyb.kindrep.ui.animation.ExerciseMediaKeys
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun SwitchSidesOverlayCard(
    state: GuidedSessionPlayerState,
    exerciseTitle: String,
    onStartNextSide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val nextSideLabel = stringResource(
        if (state.currentSide == ExerciseSide.LEFT) {
            Res.string.player_left_side
        } else {
            Res.string.player_right_side
        },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.textPrimary.copy(alpha = 0.24f))
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(palette.canvas)
                .border(1.dp, palette.border, RoundedCornerShape(18.dp))
                .padding(horizontal = 24.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(Res.string.player_switch_sides_badge, exerciseTitle.uppercase()),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = palette.secondary,
                modifier = Modifier
                    .clip(RoundedCornerShape(9.dp))
                    .background(palette.secondarySoft)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            )
            Text(
                text = stringResource(Res.string.player_switch_sides_heading, nextSideLabel),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = palette.textPrimary,
            )
            Text(
                text = stringResource(
                    Res.string.player_switch_sides_countdown,
                    state.remainingSwitchSidesSeconds,
                    state.currentExercise.targetRepetitions,
                ),
                style = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum"),
                color = palette.textMuted,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onStartNextSide,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_switch_sides_start_action, nextSideLabel),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
fun ExerciseInfoSheetOverlay(
    state: GuidedSessionPlayerState,
    exerciseCopy: ExerciseCopyResources,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.textPrimary.copy(alpha = 0.34f))
            .clickable(onClick = onDismiss)
            .padding(14.dp),
    ) {
        Text(
            text = stringResource(Res.string.player_info_paused_badge),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = palette.secondary,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(palette.secondarySoft)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .widthIn(max = 420.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(18.dp))
                .background(palette.canvas)
                .border(1.dp, palette.border, RoundedCornerShape(18.dp))
                .clickable(enabled = false, onClick = {})
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(exerciseCopy.title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = palette.textPrimary,
                    )
                    Text(
                        text = if (state.currentExercise.sets == 1) {
                            stringResource(
                                Res.string.player_break_meta_single_set_format,
                                state.currentExercise.targetRepetitions,
                            )
                        } else {
                            stringResource(
                                Res.string.player_break_meta_format,
                                state.currentExercise.targetRepetitions,
                                state.currentExercise.sets,
                            )
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.textMuted,
                    )
                }
                OutlinedButton(
                    onClick = onDismiss,
                    shape = CircleShape,
                    border = BorderStroke(1.dp, palette.border),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(36.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.player_exit_action),
                        color = palette.textPrimary,
                    )
                }
            }

            InfoInstructionCard(
                heading = stringResource(Res.string.player_info_movement_label),
                body = stringResource(exerciseCopy.setupInstruction),
                accentColor = palette.primary,
                containerColor = palette.surface,
            )
            InfoInstructionCard(
                heading = stringResource(Res.string.player_info_breathing_label),
                body = stringResource(exerciseCopy.breathingCue),
                accentColor = palette.primary,
                containerColor = palette.surface,
            )
            InfoInstructionCard(
                heading = stringResource(Res.string.player_info_safety_label),
                body = stringResource(Res.string.player_info_safety_body),
                accentColor = palette.secondary,
                containerColor = palette.secondarySoft,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_info_resume_action),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun InfoInstructionCard(
    heading: String,
    body: String,
    accentColor: Color,
    containerColor: Color,
) {
    val palette = LocalKindrepPalette.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = accentColor,
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = palette.textPrimary,
        )
    }
}

@Preview(widthDp = 874, heightDp = 402)
@Composable
private fun ExerciseInfoSheetOverlayPreview() {
    KindrepTheme {
        ExerciseInfoSheetOverlay(
            state = GuidedSessionPlayerState(
                routineName = RoutineOverview.sampleLowerBackRecovery().name,
                exercises = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises(),
                currentExerciseIndex = 1,
            ),
            exerciseCopy = ExerciseCopyCatalog.resolve(ExerciseMediaKeys.BIRD_DOG),
            onDismiss = {},
        )
    }
}
