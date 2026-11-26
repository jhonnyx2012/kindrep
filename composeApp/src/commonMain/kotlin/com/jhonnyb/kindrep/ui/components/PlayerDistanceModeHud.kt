package com.jhonnyb.kindrep.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.player_break_seconds_format
import com.jhonnyb.kindrep.generated.resources.player_exit_action
import com.jhonnyb.kindrep.generated.resources.player_hold_seconds_suffix
import com.jhonnyb.kindrep.generated.resources.player_info_action
import com.jhonnyb.kindrep.generated.resources.player_left_side
import com.jhonnyb.kindrep.generated.resources.player_next_action
import com.jhonnyb.kindrep.generated.resources.player_pause_action
import com.jhonnyb.kindrep.generated.resources.player_previous_action
import com.jhonnyb.kindrep.generated.resources.player_reps_target_format
import com.jhonnyb.kindrep.generated.resources.player_resume_action
import com.jhonnyb.kindrep.generated.resources.player_right_side
import com.jhonnyb.kindrep.generated.resources.player_session_remaining_format
import com.jhonnyb.kindrep.generated.resources.player_set_pill_format
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerIntent
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerState
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseCopyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseCopyResources
import com.jhonnyb.kindrep.ui.animation.ExerciseMediaKeys
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource
import kotlin.math.ceil

private const val PHASE_DISSOLVE_IN_MILLISECONDS = 950
private const val PHASE_DISSOLVE_OUT_MILLISECONDS = 750
private const val REP_ODOMETER_IN_MILLISECONDS = 720
private const val REP_ODOMETER_OUT_MILLISECONDS = 580
private const val FINAL_COUNTDOWN_WINDOW_SECONDS = 3.0f
private val ExerciseCountdownRingStroke = Stroke(width = 5.0f, cap = StrokeCap.Round)

@Composable
fun ActiveDistanceModeHud(
    state: GuidedSessionPlayerState,
    exerciseCopy: ExerciseCopyResources,
    remainingSessionSeconds: Int,
    remainingActiveSideSeconds: Float,
    isBetweenSetsRest: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val totalActiveSideSeconds = state.currentExercise.singleSideDurationSeconds.toFloat().coerceAtLeast(1f)
    val sideName = stringResource(
        if (state.currentSide == ExerciseSide.LEFT) {
            Res.string.player_left_side
        } else {
            Res.string.player_right_side
        },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(exerciseCopy.title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(
                    Res.string.player_session_remaining_format,
                    formatMinuteSecondDuration(remainingSessionSeconds),
                ),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFeatureSettings = "tnum",
                ),
                color = palette.textPrimary,
            )
        }

        AnimatedVisibility(
            visible = !isBetweenSetsRest && remainingActiveSideSeconds > 0.01f,
            enter = fadeIn(tween(260)),
            exit = fadeOut(tween(220)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 42.dp),
        ) {
            ExerciseCountdownRing(
                remainingSeconds = remainingActiveSideSeconds,
                totalDurationSeconds = totalActiveSideSeconds,
            )
        }

        AnimatedVisibility(
            visible = !isBetweenSetsRest,
            enter = fadeIn(tween(260)),
            exit = fadeOut(tween(220)),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    PhaseHeadlineRow(
                        phase = state.currentPhase,
                        phaseLabel = stringResource(exerciseCopy.phaseLabel(state.currentPhase)),
                        remainingHoldSeconds = state.remainingHoldSeconds,
                    )
                    Text(
                        text = stringResource(exerciseCopy.steadyFormCue),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = palette.textMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 18.dp),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    if (state.currentExercise.isBilateral || state.currentExercise.sets > 1) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            if (state.currentExercise.isBilateral) {
                                Text(
                                    text = sideName,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = palette.secondary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(palette.secondarySoft)
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                )
                            }
                            if (state.currentExercise.sets > 1) {
                                Text(
                                    text = stringResource(
                                        Res.string.player_set_pill_format,
                                        state.currentSet,
                                        state.currentExercise.sets,
                                    ),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFeatureSettings = "tnum",
                                    ),
                                    color = palette.textPrimary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(palette.canvas.copy(alpha = 0.88f))
                                        .border(1.dp, palette.border, RoundedCornerShape(50))
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                )
                            }
                        }
                    }

                    RepetitionCounterReadout(
                        currentRepetition = state.currentRepetition,
                        targetRepetitions = state.currentExercise.targetRepetitions,
                    )
                }
            }
        }
    }
}

@Composable
fun AutoHidingTransportOverlay(
    isPaused: Boolean,
    onIntent: (GuidedSessionPlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val scrimColor = if (palette.isDark) {
        palette.canvas.copy(alpha = 0.52f)
    } else {
        palette.textPrimary.copy(alpha = 0.16f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scrimColor)
            .padding(horizontal = 24.dp, vertical = 26.dp),
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = { onIntent(GuidedSessionPlayerIntent.ExitSession) },
                shape = CircleShape,
                border = BorderStroke(1.dp, palette.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = palette.surface.copy(alpha = 0.94f),
                    contentColor = palette.textPrimary,
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(44.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_exit_action),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            OutlinedButton(
                onClick = { onIntent(GuidedSessionPlayerIntent.OpenInfoSheet) },
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, palette.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = palette.surface.copy(alpha = 0.94f),
                    contentColor = palette.textPrimary,
                ),
                modifier = Modifier.height(44.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_info_action),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                onClick = { onIntent(GuidedSessionPlayerIntent.PreviousExercise) },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, palette.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = palette.surface.copy(alpha = 0.94f),
                    contentColor = palette.textPrimary,
                ),
                modifier = Modifier.height(54.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_previous_action),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Button(
                onClick = { onIntent(GuidedSessionPlayerIntent.TogglePause) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier
                    .widthIn(min = 132.dp)
                    .height(54.dp),
            ) {
                Text(
                    text = stringResource(
                        if (isPaused) Res.string.player_resume_action else Res.string.player_pause_action,
                    ),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }

            OutlinedButton(
                onClick = { onIntent(GuidedSessionPlayerIntent.NextExercise) },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, palette.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = palette.surface.copy(alpha = 0.94f),
                    contentColor = palette.textPrimary,
                ),
                modifier = Modifier.height(54.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_next_action),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun PhaseHeadlineRow(
    phase: ExercisePhase,
    phaseLabel: String,
    remainingHoldSeconds: Int,
) {
    val palette = LocalKindrepPalette.current
    val indicatorColor by animateColorAsState(
        targetValue = if (phase == ExercisePhase.HOLD) palette.secondary else palette.primary,
        animationSpec = tween(420),
        label = "PhaseIndicatorColor",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(indicatorColor),
        )
        AnimatedContent(
            targetState = phaseLabel,
            transitionSpec = {
                fadeIn(tween(PHASE_DISSOLVE_IN_MILLISECONDS, easing = LinearOutSlowInEasing))
                    .togetherWith(
                        fadeOut(tween(PHASE_DISSOLVE_OUT_MILLISECONDS, easing = LinearOutSlowInEasing)),
                    )
            },
            label = "UltraSlowPhaseDissolve",
        ) { displayedLabel ->
            Text(
                text = displayedLabel,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = palette.textPrimary,
                maxLines = 1,
            )
        }
        if (phase == ExercisePhase.HOLD && remainingHoldSeconds > 0) {
            Text(
                text = stringResource(Res.string.player_hold_seconds_suffix, remainingHoldSeconds),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum",
                ),
                color = palette.secondary,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun RepetitionCounterReadout(
    currentRepetition: Int,
    targetRepetitions: Int,
) {
    val palette = LocalKindrepPalette.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AnimatedContent(
            targetState = currentRepetition,
            transitionSpec = {
                (fadeIn(tween(REP_ODOMETER_IN_MILLISECONDS, easing = FastOutSlowInEasing)) +
                    slideInVertically(tween(REP_ODOMETER_IN_MILLISECONDS, easing = FastOutSlowInEasing)) { it / 2 })
                    .togetherWith(
                        fadeOut(tween(REP_ODOMETER_OUT_MILLISECONDS, easing = FastOutSlowInEasing)) +
                            slideOutVertically(tween(REP_ODOMETER_OUT_MILLISECONDS, easing = FastOutSlowInEasing)) { -it / 2 },
                    )
            },
            label = "RepetitionOdometerRoll",
        ) { displayedRep ->
            Text(
                text = displayedRep.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 52.sp,
                    lineHeight = 52.sp,
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum",
                ),
                color = palette.textPrimary,
            )
        }
        Text(
            text = stringResource(Res.string.player_reps_target_format, targetRepetitions),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold,
                fontFeatureSettings = "tnum",
            ),
            color = palette.textMuted,
        )
    }
}

@Composable
private fun ExerciseCountdownRing(
    remainingSeconds: Float,
    totalDurationSeconds: Float,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val safeTotal = totalDurationSeconds.coerceAtLeast(1f)
    val clampedRemaining = remainingSeconds.coerceIn(0f, safeTotal)
    val isFinalThreeSeconds = clampedRemaining <= FINAL_COUNTDOWN_WINDOW_SECONDS
    val sweepFraction = if (isFinalThreeSeconds) {
        (clampedRemaining / FINAL_COUNTDOWN_WINDOW_SECONDS).coerceIn(0f, 1f)
    } else {
        (clampedRemaining / safeTotal).coerceIn(0f, 1f)
    }
    val displayedSecond = ceil(clampedRemaining).toInt().coerceAtLeast(1)

    val ringScale by animateFloatAsState(
        targetValue = if (isFinalThreeSeconds) 1.12f else 1.0f,
        animationSpec = tween(durationMillis = 340, easing = FastOutSlowInEasing),
        label = "ExerciseRingScale",
    )
    val arcColor by animateColorAsState(
        targetValue = if (isFinalThreeSeconds) palette.secondary else palette.primary,
        animationSpec = tween(durationMillis = 320),
        label = "ExerciseRingArcColor",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isFinalThreeSeconds) palette.secondary else palette.border,
        animationSpec = tween(durationMillis = 320),
        label = "ExerciseRingBorderColor",
    )
    val digitColor by animateColorAsState(
        targetValue = if (isFinalThreeSeconds) palette.secondary else palette.textPrimary,
        animationSpec = tween(durationMillis = 320),
        label = "ExerciseRingDigitColor",
    )

    val pulseProgress = if (isFinalThreeSeconds) {
        val transition = rememberInfiniteTransition(label = "ExerciseRingFinishPulse")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1_000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "ExerciseRingPulseProgress",
        ).value
    } else {
        0f
    }

    Box(
        modifier = modifier
            .size(62.dp)
            .graphicsLayer {
                scaleX = ringScale
                scaleY = ringScale
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isFinalThreeSeconds) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val baseRadius = size.minDimension / 2f
                val pulseRadius = baseRadius + 10.dp.toPx() * pulseProgress
                drawCircle(
                    color = palette.secondary.copy(alpha = 0.36f * (1f - pulseProgress)),
                    radius = pulseRadius,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(palette.canvas.copy(alpha = 0.88f))
                .border(1.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            ) {
                drawArc(
                    color = palette.matFill,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = ExerciseCountdownRingStroke,
                )
                drawArc(
                    color = arcColor,
                    startAngle = -90f,
                    sweepAngle = 360f * sweepFraction,
                    useCenter = false,
                    style = ExerciseCountdownRingStroke,
                )
            }
            Text(
                text = if (isFinalThreeSeconds) {
                    displayedSecond.toString()
                } else {
                    stringResource(Res.string.player_break_seconds_format, displayedSecond)
                },
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = if (isFinalThreeSeconds) 24.sp else 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum",
                ),
                color = digitColor,
            )
        }
    }
}

private fun formatMinuteSecondDuration(totalSeconds: Int): String {
    val clampedSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = clampedSeconds / 60
    val seconds = clampedSeconds % 60
    val paddedSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
    return "$minutes:$paddedSeconds"
}

@Preview(widthDp = 874, heightDp = 402, showBackground = true)
@Composable
private fun ActiveDistanceModeHudPreview() {
    KindrepTheme {
        ActiveDistanceModeHud(
            state = GuidedSessionPlayerState(
                routineName = RoutineOverview.sampleLowerBackRecovery().name,
                exercises = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises(),
                currentExerciseIndex = 1,
            ),
            exerciseCopy = ExerciseCopyCatalog.resolve(ExerciseMediaKeys.BIRD_DOG),
            remainingSessionSeconds = 462,
            remainingActiveSideSeconds = 2.4f,
        )
    }
}
