package com.jhonnyb.kindrep.presentation.player

import androidx.lifecycle.ViewModel
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.GuidedExerciseItem
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.domain.service.AudioCueKeys
import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class GuidedSessionPlayerViewModel(
    private val audioCueService: AudioCueService,
    routineName: String = RoutineOverview.sampleLowerBackRecovery().name,
    exercises: List<GuidedExerciseItem> = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises(),
    breakDurationSeconds: Int = GuidedSessionPlayerState.DEFAULT_BREAK_SECONDS,
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        GuidedSessionPlayerState(
            routineName = routineName,
            exercises = exercises,
            breakDurationSeconds = breakDurationSeconds,
            totalBreakSeconds = breakDurationSeconds,
            remainingBreakSeconds = breakDurationSeconds,
        ),
    )
    val state: StateFlow<GuidedSessionPlayerState> = mutableState.asStateFlow()

    private val effectChannel = Channel<GuidedSessionPlayerEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<GuidedSessionPlayerEffect> = effectChannel.receiveAsFlow()

    private var wasPausedBeforeInfoSheet: Boolean = false
    private var lastPhaseChangeMilliseconds: Long =
        -GuidedSessionPlayerState.MINIMUM_PHASE_CHANGE_INTERVAL_MILLISECONDS

    fun onIntent(intent: GuidedSessionPlayerIntent) {
        when (intent) {
            GuidedSessionPlayerIntent.TogglePause -> handleTogglePause()
            GuidedSessionPlayerIntent.PreviousExercise -> handlePreviousExercise()
            GuidedSessionPlayerIntent.NextExercise -> handleNextExercise()
            GuidedSessionPlayerIntent.ToggleControls -> handleToggleControls()
            GuidedSessionPlayerIntent.HideControls -> handleHideControls()
            GuidedSessionPlayerIntent.OpenInfoSheet -> handleOpenInfoSheet()
            GuidedSessionPlayerIntent.DismissInfoSheet -> handleDismissInfoSheet()
            GuidedSessionPlayerIntent.AddBreakTime -> handleAddBreakTime()
            GuidedSessionPlayerIntent.SkipBreak -> handleStartActiveExercise()
            GuidedSessionPlayerIntent.StartNextSide -> handleStartActiveExercise()
            GuidedSessionPlayerIntent.CompleteRepetitionCycle -> handleCompleteRepetitionCycle()
            is GuidedSessionPlayerIntent.UpdatePlaybackPhase -> handleUpdatePlaybackPhase(intent)
            GuidedSessionPlayerIntent.TickTimerSecond -> handleTickTimerSecond()
            GuidedSessionPlayerIntent.ExitSession -> effectChannel.trySend(
                GuidedSessionPlayerEffect.NavigateBack,
            )
        }
    }

    private fun handleTogglePause() {
        mutableState.update { currentState ->
            val nextPaused = !currentState.isPaused
            currentState.copy(
                isPaused = nextPaused,
                isControlsVisible = if (nextPaused) true else currentState.isControlsVisible,
            )
        }
    }

    private fun handlePreviousExercise() {
        val currentState = mutableState.value
        val previousIndex = (currentState.currentExerciseIndex - 1).coerceAtLeast(0)
        if (previousIndex == currentState.currentExerciseIndex && currentState.currentRepetition == 1) {
            return
        }
        resetPhaseThrottle()
        mutableState.update {
            it.copy(
                currentExerciseIndex = previousIndex,
                mode = GuidedPlayerMode.ACTIVE,
                currentSide = ExerciseSide.LEFT,
                currentRepetition = 1,
                currentPhase = ExercisePhase.EXTEND,
                remainingHoldSeconds = 0,
                isInfoSheetVisible = false,
            )
        }
    }

    private fun handleNextExercise() {
        val currentState = mutableState.value
        val nextIndex = currentState.currentExerciseIndex + 1
        if (nextIndex >= currentState.exercises.size) {
            finishSession()
            return
        }
        enterBreakForExercise(nextIndex)
    }

    private fun handleToggleControls() {
        val currentState = mutableState.value
        if (currentState.mode != GuidedPlayerMode.ACTIVE || currentState.isInfoSheetVisible) {
            return
        }
        mutableState.update { it.copy(isControlsVisible = !it.isControlsVisible) }
    }

    private fun handleHideControls() {
        val currentState = mutableState.value
        if (currentState.isPaused || currentState.isInfoSheetVisible) return
        mutableState.update { it.copy(isControlsVisible = false) }
    }

    private fun handleOpenInfoSheet() {
        val currentState = mutableState.value
        if (currentState.isInfoSheetVisible) return
        wasPausedBeforeInfoSheet = currentState.isPaused
        mutableState.update {
            it.copy(
                isPaused = true,
                isInfoSheetVisible = true,
                isControlsVisible = false,
            )
        }
    }

    private fun handleDismissInfoSheet() {
        val currentState = mutableState.value
        if (!currentState.isInfoSheetVisible) return
        mutableState.update {
            it.copy(
                isPaused = wasPausedBeforeInfoSheet,
                isInfoSheetVisible = false,
            )
        }
    }

    private fun handleAddBreakTime() {
        val currentState = mutableState.value
        if (currentState.mode != GuidedPlayerMode.BREAK) return
        val updatedRemaining = currentState.remainingBreakSeconds +
            GuidedSessionPlayerState.BREAK_EXTENSION_SECONDS
        mutableState.update {
            it.copy(
                remainingBreakSeconds = updatedRemaining,
                totalBreakSeconds = maxOf(it.totalBreakSeconds, updatedRemaining),
            )
        }
    }

    private fun handleStartActiveExercise() {
        resetPhaseThrottle()
        mutableState.update {
            it.copy(
                mode = GuidedPlayerMode.ACTIVE,
                currentRepetition = 1,
                currentPhase = ExercisePhase.EXTEND,
                remainingHoldSeconds = 0,
                isControlsVisible = false,
            )
        }
    }

    private fun handleCompleteRepetitionCycle() {
        val currentState = mutableState.value
        if (currentState.mode != GuidedPlayerMode.ACTIVE) return
        val activeExercise = currentState.currentExercise
        if (currentState.currentRepetition < activeExercise.targetRepetitions) {
            mutableState.update { it.copy(currentRepetition = it.currentRepetition + 1) }
            return
        }

        if (activeExercise.isBilateral && currentState.currentSide == ExerciseSide.LEFT) {
            audioCueService.playCue(AudioCueKeys.SWITCH_SIDES)
            resetPhaseThrottle()
            mutableState.update {
                it.copy(
                    mode = GuidedPlayerMode.SWITCH_SIDES,
                    currentSide = ExerciseSide.RIGHT,
                    currentRepetition = 1,
                    remainingSwitchSidesSeconds = GuidedSessionPlayerState.SWITCH_SIDES_DURATION_SECONDS,
                    currentPhase = ExercisePhase.EXTEND,
                    remainingHoldSeconds = 0,
                    isControlsVisible = false,
                )
            }
            return
        }

        val nextIndex = currentState.currentExerciseIndex + 1
        if (nextIndex < currentState.exercises.size) {
            enterBreakForExercise(nextIndex)
        } else {
            finishSession()
        }
    }

    private fun handleUpdatePlaybackPhase(intent: GuidedSessionPlayerIntent.UpdatePlaybackPhase) {
        val currentState = mutableState.value
        if (currentState.mode != GuidedPlayerMode.ACTIVE) return

        val phaseChanged = intent.phase != currentState.currentPhase
        if (phaseChanged) {
            val elapsedSinceLastChange = intent.elapsedMilliseconds - lastPhaseChangeMilliseconds
            if (elapsedSinceLastChange < GuidedSessionPlayerState.MINIMUM_PHASE_CHANGE_INTERVAL_MILLISECONDS) {
                return
            }
            lastPhaseChangeMilliseconds = intent.elapsedMilliseconds
            mutableState.update {
                it.copy(
                    currentPhase = intent.phase,
                    remainingHoldSeconds = intent.remainingHoldSeconds,
                )
            }
        } else if (intent.remainingHoldSeconds != currentState.remainingHoldSeconds) {
            mutableState.update {
                it.copy(remainingHoldSeconds = intent.remainingHoldSeconds)
            }
        }
    }

    private fun handleTickTimerSecond() {
        val currentState = mutableState.value
        when (currentState.mode) {
            GuidedPlayerMode.SWITCH_SIDES -> {
                val nextRemaining = currentState.remainingSwitchSidesSeconds - 1
                if (nextRemaining <= 0) {
                    handleStartActiveExercise()
                } else {
                    mutableState.update { it.copy(remainingSwitchSidesSeconds = nextRemaining) }
                }
            }

            GuidedPlayerMode.BREAK -> {
                val nextRemaining = currentState.remainingBreakSeconds - 1
                if (nextRemaining <= 0) {
                    handleStartActiveExercise()
                } else {
                    mutableState.update { it.copy(remainingBreakSeconds = nextRemaining) }
                }
            }

            GuidedPlayerMode.ACTIVE -> Unit
        }
    }

    private fun enterBreakForExercise(nextIndex: Int) {
        audioCueService.playCue(AudioCueKeys.BREAK_START)
        resetPhaseThrottle()
        mutableState.update { currentState ->
            currentState.copy(
                currentExerciseIndex = nextIndex,
                mode = GuidedPlayerMode.BREAK,
                currentSide = ExerciseSide.LEFT,
                currentRepetition = 1,
                totalBreakSeconds = currentState.breakDurationSeconds,
                remainingBreakSeconds = currentState.breakDurationSeconds,
                currentPhase = ExercisePhase.EXTEND,
                remainingHoldSeconds = 0,
                isInfoSheetVisible = false,
                isControlsVisible = false,
            )
        }
    }

    private fun finishSession() {
        audioCueService.playCue(AudioCueKeys.SESSION_COMPLETE)
        effectChannel.trySend(
            GuidedSessionPlayerEffect.NavigateToPostSessionCheckIn(
                routineName = mutableState.value.routineName,
            ),
        )
    }

    private fun resetPhaseThrottle() {
        lastPhaseChangeMilliseconds = -GuidedSessionPlayerState.MINIMUM_PHASE_CHANGE_INTERVAL_MILLISECONDS
    }
}
