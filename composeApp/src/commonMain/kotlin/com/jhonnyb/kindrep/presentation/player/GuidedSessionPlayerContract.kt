package com.jhonnyb.kindrep.presentation.player

import androidx.compose.runtime.Immutable
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.GuidedExerciseItem

enum class GuidedPlayerMode {
    ACTIVE,
    SWITCH_SIDES,
    BREAK,
}

@Immutable
data class GuidedSessionPlayerState(
    val routineName: String,
    val exercises: List<GuidedExerciseItem>,
    val currentExerciseIndex: Int = 0,
    val mode: GuidedPlayerMode = GuidedPlayerMode.ACTIVE,
    val currentSide: ExerciseSide = ExerciseSide.LEFT,
    val currentSet: Int = 1,
    val currentRepetition: Int = 1,
    val isBetweenSetsBreak: Boolean = false,
    val isPaused: Boolean = false,
    val isInfoSheetVisible: Boolean = false,
    val isControlsVisible: Boolean = true,
    val breakDurationSeconds: Int = DEFAULT_BREAK_SECONDS,
    val totalBreakSeconds: Int = DEFAULT_BREAK_SECONDS,
    val remainingBreakSeconds: Int = DEFAULT_BREAK_SECONDS,
    val remainingSwitchSidesSeconds: Int = SWITCH_SIDES_DURATION_SECONDS,
    val currentPhase: ExercisePhase = ExercisePhase.EXTEND,
    val remainingHoldSeconds: Int = 0,
) {
    val currentExercise: GuidedExerciseItem
        get() = exercises[currentExerciseIndex.coerceIn(0, exercises.lastIndex)]

    val totalExercises: Int
        get() = exercises.size

    val isBetweenSetsRest: Boolean
        get() = mode == GuidedPlayerMode.BREAK && isBetweenSetsBreak

    val isUpNextBreak: Boolean
        get() = mode == GuidedPlayerMode.BREAK && !isBetweenSetsBreak

    val totalSessionDurationSeconds: Int
        get() = exercises.sumOf { it.totalDurationSeconds(breakDurationSeconds) } +
            (exercises.size - 1).coerceAtLeast(0) * breakDurationSeconds

    fun remainingSessionSeconds(currentCycleProgress: Float): Int {
        val futureExercisesSeconds = exercises
            .drop(currentExerciseIndex + 1)
            .sumOf { it.totalDurationSeconds(breakDurationSeconds) }
        val futureExerciseBreaksCount = (exercises.size - 1 - currentExerciseIndex).coerceAtLeast(0)
        val futureExerciseBreaksSeconds = futureExerciseBreaksCount * breakDurationSeconds

        val safeTotalSets = currentExercise.sets.coerceAtLeast(1)
        val remainingSetsAfterCurrent = (safeTotalSets - currentSet).coerceAtLeast(0)
        val futureSetsInCurrentExerciseSeconds = remainingSetsAfterCurrent *
            (currentExercise.singleSetDurationSeconds + breakDurationSeconds)

        val currentExerciseRemainingSeconds = when (mode) {
            GuidedPlayerMode.BREAK -> {
                if (isBetweenSetsBreak) {
                    remainingBreakSeconds +
                        currentExercise.singleSetDurationSeconds +
                        futureSetsInCurrentExerciseSeconds
                } else {
                    remainingBreakSeconds + currentExercise.totalDurationSeconds(breakDurationSeconds)
                }
            }

            GuidedPlayerMode.SWITCH_SIDES -> {
                remainingSwitchSidesSeconds +
                    currentExercise.singleSideDurationSeconds +
                    futureSetsInCurrentExerciseSeconds
            }

            GuidedPlayerMode.ACTIVE -> {
                val activeSideDuration = currentExercise.singleSideDurationSeconds
                val completedRepsInSide = (currentRepetition - 1).coerceAtLeast(0)
                val elapsedInCurrentSide = (completedRepsInSide + currentCycleProgress.coerceIn(0f, 1f)) *
                    currentExercise.repetitionPaceSeconds
                val remainingInCurrentSide = (activeSideDuration - elapsedInCurrentSide)
                    .coerceAtLeast(0f)
                val secondSideAndSwitch = if (currentExercise.isBilateral && currentSide == ExerciseSide.LEFT) {
                    SWITCH_SIDES_DURATION_SECONDS + activeSideDuration
                } else {
                    0
                }
                kotlin.math.ceil(remainingInCurrentSide).toInt() +
                    secondSideAndSwitch +
                    futureSetsInCurrentExerciseSeconds
            }
        }
        return (currentExerciseRemainingSeconds + futureExercisesSeconds + futureExerciseBreaksSeconds)
            .coerceAtLeast(0)
    }

    fun remainingActiveSideSeconds(currentCycleProgress: Float): Float {
        if (mode != GuidedPlayerMode.ACTIVE) return 0f
        val activeSideDuration = currentExercise.singleSideDurationSeconds.toFloat()
        val completedRepsInSide = (currentRepetition - 1).coerceAtLeast(0)
        val elapsedInCurrentSide = (completedRepsInSide + currentCycleProgress.coerceIn(0f, 1f)) *
            currentExercise.repetitionPaceSeconds
        return (activeSideDuration - elapsedInCurrentSide).coerceAtLeast(0f)
    }

    fun currentExerciseProgressFraction(currentCycleProgress: Float): Float {
        val total = currentExercise.totalDurationSeconds(breakDurationSeconds).coerceAtLeast(1).toFloat()
        val singleSetDuration = currentExercise.singleSetDurationSeconds.toFloat()
        val activeSideDuration = currentExercise.singleSideDurationSeconds.toFloat()
        val completedSetsCount = (currentSet - 1).coerceAtLeast(0)

        val elapsed = when (mode) {
            GuidedPlayerMode.BREAK -> {
                if (isBetweenSetsBreak) {
                    val elapsedBreak = (totalBreakSeconds - remainingBreakSeconds).coerceAtLeast(0)
                    completedSetsCount * singleSetDuration +
                        (completedSetsCount - 1).coerceAtLeast(0) * breakDurationSeconds +
                        elapsedBreak
                } else {
                    0f
                }
            }

            GuidedPlayerMode.SWITCH_SIDES -> {
                val priorSetsElapsed = completedSetsCount * (singleSetDuration + breakDurationSeconds)
                priorSetsElapsed + activeSideDuration +
                    (SWITCH_SIDES_DURATION_SECONDS - remainingSwitchSidesSeconds)
            }

            GuidedPlayerMode.ACTIVE -> {
                val priorSetsElapsed = completedSetsCount * (singleSetDuration + breakDurationSeconds)
                val priorSideOffset = if (currentExercise.isBilateral && currentSide == ExerciseSide.RIGHT) {
                    activeSideDuration + SWITCH_SIDES_DURATION_SECONDS
                } else {
                    0f
                }
                val completedReps = (currentRepetition - 1).coerceAtLeast(0)
                priorSetsElapsed + priorSideOffset +
                    (completedReps + currentCycleProgress.coerceIn(0f, 1f)) *
                    currentExercise.repetitionPaceSeconds
            }
        }
        return (elapsed / total).coerceIn(0f, 1f)
    }

    val breakProgressFraction: Float
        get() = if (totalBreakSeconds <= 0) {
            1f
        } else {
            (1f - remainingBreakSeconds.toFloat() / totalBreakSeconds.toFloat()).coerceIn(0f, 1f)
        }

    companion object {
        const val DEFAULT_BREAK_SECONDS: Int = 10
        const val SWITCH_SIDES_DURATION_SECONDS: Int = 3
        const val BREAK_EXTENSION_SECONDS: Int = 5
        const val MINIMUM_PHASE_CHANGE_INTERVAL_MILLISECONDS: Long = 1_000L
    }
}

sealed interface GuidedSessionPlayerIntent {
    data object TogglePause : GuidedSessionPlayerIntent
    data object PreviousExercise : GuidedSessionPlayerIntent
    data object NextExercise : GuidedSessionPlayerIntent
    data object ToggleControls : GuidedSessionPlayerIntent
    data object HideControls : GuidedSessionPlayerIntent
    data object OpenInfoSheet : GuidedSessionPlayerIntent
    data object DismissInfoSheet : GuidedSessionPlayerIntent
    data object AddBreakTime : GuidedSessionPlayerIntent
    data object SkipBreak : GuidedSessionPlayerIntent
    data object StartNextSide : GuidedSessionPlayerIntent
    data object CompleteRepetitionCycle : GuidedSessionPlayerIntent
    data class UpdatePlaybackPhase(
        val phase: ExercisePhase,
        val remainingHoldSeconds: Int,
        val elapsedMilliseconds: Long,
    ) : GuidedSessionPlayerIntent

    data object TickTimerSecond : GuidedSessionPlayerIntent
    data object ExitSession : GuidedSessionPlayerIntent
}

sealed interface GuidedSessionPlayerEffect {
    data class NavigateToPostSessionCheckIn(val routineName: String) : GuidedSessionPlayerEffect
    data object NavigateBack : GuidedSessionPlayerEffect
}
