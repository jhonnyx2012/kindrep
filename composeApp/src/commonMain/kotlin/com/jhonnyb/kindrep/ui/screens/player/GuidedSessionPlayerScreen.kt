package com.jhonnyb.kindrep.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.domain.usecase.ExercisePoseEvaluator
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.player_break_add_five_seconds
import com.jhonnyb.kindrep.generated.resources.player_break_label
import com.jhonnyb.kindrep.generated.resources.player_break_meta_format
import com.jhonnyb.kindrep.generated.resources.player_break_meta_single_set_format
import com.jhonnyb.kindrep.generated.resources.player_break_seconds_format
import com.jhonnyb.kindrep.generated.resources.player_break_skip_action
import com.jhonnyb.kindrep.generated.resources.player_up_next_badge
import com.jhonnyb.kindrep.presentation.player.GuidedPlayerMode
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerIntent
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerState
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseCopyCatalog
import com.jhonnyb.kindrep.ui.animation.ExerciseCopyResources
import com.jhonnyb.kindrep.ui.components.ActiveDistanceModeHud
import com.jhonnyb.kindrep.ui.components.AutoHidingTransportOverlay
import com.jhonnyb.kindrep.ui.components.ExerciseAnimationStage
import com.jhonnyb.kindrep.ui.dialogs.ExerciseInfoSheetOverlay
import com.jhonnyb.kindrep.ui.dialogs.SwitchSidesOverlayCard
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.math.ceil

private const val STAGE_MORPH_DURATION_MILLISECONDS = 560
private const val CONTROLS_AUTO_HIDE_MILLISECONDS = 3_000L
private const val TIMER_TICK_MILLISECONDS = 1_000L
private val BreakRingStroke = Stroke(width = 6f, cap = StrokeCap.Round)

@Composable
fun GuidedSessionPlayerScreen(
    state: GuidedSessionPlayerState,
    onIntent: (GuidedSessionPlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val cycleProgressState = remember { mutableFloatStateOf(0f) }
    val activeExercise = state.currentExercise
    val activeSpec = remember(activeExercise.mediaKey) {
        ExerciseChoreographyCatalog.resolveSpec(activeExercise.mediaKey)
    }
    val exerciseCopy = remember(activeExercise.mediaKey) {
        ExerciseCopyCatalog.resolve(activeExercise.mediaKey)
    }

    LaunchedEffect(
        state.currentExerciseIndex,
        state.currentSide,
        state.mode,
        state.isPaused,
        state.isInfoSheetVisible,
    ) {
        val shouldAnimate = (state.mode == GuidedPlayerMode.ACTIVE && !state.isPaused && !state.isInfoSheetVisible) ||
            state.mode == GuidedPlayerMode.BREAK
        if (!shouldAnimate) return@LaunchedEffect

        var previousFrameNanos = withFrameNanos { it }
        var elapsedActiveMilliseconds = 0L
        while (true) {
            val currentFrameNanos = withFrameNanos { it }
            val deltaSeconds = ((currentFrameNanos - previousFrameNanos) / 1_000_000_000f)
                .coerceIn(0f, 0.05f)
            previousFrameNanos = currentFrameNanos
            elapsedActiveMilliseconds += (deltaSeconds * 1_000f).toLong()

            val nextProgress = cycleProgressState.floatValue + deltaSeconds / activeExercise.repetitionPaceSeconds
            if (nextProgress >= 1f) {
                cycleProgressState.floatValue = nextProgress % 1f
                if (state.mode == GuidedPlayerMode.ACTIVE) {
                    onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
                }
            } else {
                cycleProgressState.floatValue = nextProgress
            }

            if (state.mode == GuidedPlayerMode.ACTIVE) {
                val frame = ExercisePoseEvaluator.evaluateFrame(
                    spec = activeSpec,
                    cycleProgress = cycleProgressState.floatValue,
                    peakHoldRatio = activeExercise.peakHoldRatio,
                    activeSide = state.currentSide,
                )
                val remainingHoldSeconds = if (frame.phase == ExercisePhase.HOLD) {
                    ceil((1f - frame.holdProgress) * frame.holdSpanRatio * activeExercise.repetitionPaceSeconds)
                        .toInt()
                        .coerceAtLeast(1)
                } else {
                    0
                }
                onIntent(
                    GuidedSessionPlayerIntent.UpdatePlaybackPhase(
                        phase = frame.phase,
                        remainingHoldSeconds = remainingHoldSeconds,
                        elapsedMilliseconds = elapsedActiveMilliseconds,
                    ),
                )
            }
        }
    }

    LaunchedEffect(state.currentExerciseIndex, state.currentSide, state.mode) {
        cycleProgressState.floatValue = 0f
    }

    LaunchedEffect(state.mode, state.isPaused) {
        if (state.mode == GuidedPlayerMode.BREAK || state.mode == GuidedPlayerMode.SWITCH_SIDES) {
            while (true) {
                delay(TIMER_TICK_MILLISECONDS)
                onIntent(GuidedSessionPlayerIntent.TickTimerSecond)
            }
        }
    }

    LaunchedEffect(state.isControlsVisible, state.isPaused, state.mode) {
        if (state.mode == GuidedPlayerMode.ACTIVE && state.isControlsVisible && !state.isPaused) {
            delay(CONTROLS_AUTO_HIDE_MILLISECONDS)
            onIntent(GuidedSessionPlayerIntent.HideControls)
        }
    }

    val stageTapInteraction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.canvas)
            .safeDrawingPadding()
            .clickable(
                interactionSource = stageTapInteraction,
                indication = null,
                onClick = { onIntent(GuidedSessionPlayerIntent.ToggleControls) },
            ),
    ) {
        MorphingAnimationStageContainer(
            isBreakMode = state.mode == GuidedPlayerMode.BREAK,
            mediaKey = activeExercise.mediaKey,
            peakHoldRatio = activeExercise.peakHoldRatio,
            activeSide = state.currentSide,
            cycleProgressProvider = { cycleProgressState.floatValue },
        )

        SessionProgressSegmentsRow(
            totalExercises = state.totalExercises,
            currentExerciseIndex = state.currentExerciseIndex,
            activeExerciseProgressProvider = {
                state.currentExerciseProgressFraction(cycleProgressState.floatValue)
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
        )

        AnimatedVisibility(
            visible = state.mode == GuidedPlayerMode.ACTIVE,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(240)),
        ) {
            ActiveDistanceModeHud(
                state = state,
                exerciseCopy = exerciseCopy,
                remainingSessionSeconds = state.remainingSessionSeconds(cycleProgressState.floatValue),
                remainingActiveSideSeconds = state.remainingActiveSideSeconds(cycleProgressState.floatValue),
            )
        }

        AnimatedVisibility(
            visible = state.mode == GuidedPlayerMode.ACTIVE && state.isControlsVisible && !state.isInfoSheetVisible,
            enter = fadeIn(tween(260)),
            exit = fadeOut(tween(260)),
        ) {
            AutoHidingTransportOverlay(isPaused = state.isPaused, onIntent = onIntent)
        }

        AnimatedVisibility(
            visible = state.mode == GuidedPlayerMode.BREAK,
            enter = fadeIn(tween(380, delayMillis = 120)),
            exit = fadeOut(tween(220)),
        ) {
            UpNextBreakPanel(state = state, exerciseCopy = exerciseCopy, onIntent = onIntent)
        }

        AnimatedVisibility(
            visible = state.mode == GuidedPlayerMode.SWITCH_SIDES,
            enter = fadeIn(tween(280)),
            exit = fadeOut(tween(240)),
        ) {
            SwitchSidesOverlayCard(
                state = state,
                exerciseTitle = stringResource(exerciseCopy.title),
                onStartNextSide = { onIntent(GuidedSessionPlayerIntent.StartNextSide) },
            )
        }

        AnimatedVisibility(
            visible = state.isInfoSheetVisible,
            enter = fadeIn(tween(260)),
            exit = fadeOut(tween(220)),
        ) {
            ExerciseInfoSheetOverlay(
                state = state,
                exerciseCopy = exerciseCopy,
                onDismiss = { onIntent(GuidedSessionPlayerIntent.DismissInfoSheet) },
            )
        }
    }
}

@Composable
private fun MorphingAnimationStageContainer(
    isBreakMode: Boolean,
    mediaKey: String,
    peakHoldRatio: Float,
    activeSide: ExerciseSide,
    cycleProgressProvider: () -> Float,
) {
    val palette = LocalKindrepPalette.current
    val morphFraction by animateFloatAsState(
        targetValue = if (isBreakMode) 1f else 0f,
        animationSpec = tween(
            durationMillis = STAGE_MORPH_DURATION_MILLISECONDS,
            easing = FastOutSlowInEasing,
        ),
        label = "StageBreakMorph",
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val fullWidth = maxWidth
        val fullHeight = maxHeight

        val targetLeft = fullWidth * 0.49f
        val targetTop = 36.dp
        val targetWidth = (fullWidth * 0.48f).coerceAtLeast(160.dp)
        val targetHeight = (fullHeight - 56.dp).coerceAtLeast(140.dp)

        val currentLeft = targetLeft * morphFraction
        val currentTop = targetTop * morphFraction
        val currentWidth = fullWidth + (targetWidth - fullWidth) * morphFraction
        val currentHeight = fullHeight + (targetHeight - fullHeight) * morphFraction
        val currentCornerRadius = 16.dp * morphFraction
        val topFigurePadding = 64.dp * (1f - morphFraction) + 16.dp * morphFraction
        val bottomFigurePadding = 108.dp * (1f - morphFraction) + 16.dp * morphFraction

        Box(
            modifier = Modifier
                .offset { IntOffset(currentLeft.roundToPx(), currentTop.roundToPx()) }
                .size(width = currentWidth, height = currentHeight)
                .clip(RoundedCornerShape(currentCornerRadius))
                .background(palette.surfaceVariant)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = topFigurePadding,
                    bottom = bottomFigurePadding,
                ),
        ) {
            ExerciseAnimationStage(
                mediaKey = mediaKey,
                peakHoldRatio = peakHoldRatio,
                activeSide = activeSide,
                cycleProgressProvider = cycleProgressProvider,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SessionProgressSegmentsRow(
    totalExercises: Int,
    currentExerciseIndex: Int,
    activeExerciseProgressProvider: () -> Float,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        for (index in 0 until totalExercises) {
            if (index == currentExerciseIndex) {
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                ) {
                    drawRect(color = palette.surface)
                    val progressFraction = activeExerciseProgressProvider().coerceIn(0f, 1f)
                    if (progressFraction > 0f) {
                        drawRect(
                            color = palette.secondary,
                            size = size.copy(width = size.width * progressFraction),
                        )
                    }
                }
            } else {
                val segmentColor by animateColorAsState(
                    targetValue = if (index < currentExerciseIndex) palette.primary else palette.surface,
                    animationSpec = tween(360),
                    label = "ProgressSegmentColor",
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(segmentColor),
                )
            }
        }
    }
}

@Composable
private fun UpNextBreakPanel(
    state: GuidedSessionPlayerState,
    exerciseCopy: ExerciseCopyResources,
    onIntent: (GuidedSessionPlayerIntent) -> Unit,
) {
    val palette = LocalKindrepPalette.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.47f)
            .padding(start = 24.dp, top = 36.dp, bottom = 20.dp, end = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            BreakCountdownRing(
                remainingSeconds = state.remainingBreakSeconds,
                progressFraction = state.breakProgressFraction,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(
                        Res.string.player_up_next_badge,
                        state.currentExerciseIndex + 1,
                        state.totalExercises,
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = palette.secondary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(palette.secondarySoft)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textMuted,
                )
            }
        }

        Text(
            text = stringResource(exerciseCopy.setupInstruction),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = palette.textPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(palette.surface)
                .border(1.dp, palette.border, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                onClick = { onIntent(GuidedSessionPlayerIntent.AddBreakTime) },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, palette.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = palette.surface,
                    contentColor = palette.textPrimary,
                ),
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_break_add_five_seconds),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { onIntent(GuidedSessionPlayerIntent.SkipBreak) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_break_skip_action),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun BreakCountdownRing(
    remainingSeconds: Int,
    progressFraction: Float,
) {
    val palette = LocalKindrepPalette.current
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(420, easing = LinearOutSlowInEasing),
        label = "BreakRingProgress",
    )

    Box(
        modifier = Modifier.size(84.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = palette.matFill,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = BreakRingStroke,
            )
            drawArc(
                color = palette.primary,
                startAngle = -90f,
                sweepAngle = 360f * (1f - animatedProgress),
                useCenter = false,
                style = BreakRingStroke,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.player_break_seconds_format, remainingSeconds),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum",
                ),
                color = palette.primary,
            )
            Text(
                text = stringResource(Res.string.player_break_label),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = palette.textMuted,
            )
        }
    }
}

@Preview(widthDp = 874, heightDp = 402)
@Composable
private fun GuidedSessionPlayerScreenPreview() {
    KindrepTheme {
        GuidedSessionPlayerScreen(
            state = GuidedSessionPlayerState(
                routineName = RoutineOverview.sampleLowerBackRecovery().name,
                exercises = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises(),
                currentExerciseIndex = 1,
                isControlsVisible = false,
            ),
            onIntent = {},
        )
    }
}
