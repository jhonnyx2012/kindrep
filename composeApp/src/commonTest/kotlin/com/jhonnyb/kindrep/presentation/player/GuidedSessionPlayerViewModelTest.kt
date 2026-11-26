package com.jhonnyb.kindrep.presentation.player

import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.domain.model.ExerciseSide
import com.jhonnyb.kindrep.domain.model.GuidedExerciseItem
import com.jhonnyb.kindrep.domain.service.AudioCueKeys
import com.jhonnyb.kindrep.fakes.FakeAudioCueService
import com.jhonnyb.kindrep.ui.animation.ExerciseMediaKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GuidedSessionPlayerViewModelTest {

    private val bilateralFirstExercise = GuidedExerciseItem(
        identifier = "ex-1",
        mediaKey = ExerciseMediaKeys.BIRD_DOG,
        targetRepetitions = 2,
        sets = 1,
        repetitionPaceSeconds = 6.0f,
        peakHoldRatio = 0.35f,
        isBilateral = true,
    )

    private val unilateralSecondExercise = GuidedExerciseItem(
        identifier = "ex-2",
        mediaKey = ExerciseMediaKeys.GLUTE_BRIDGE,
        targetRepetitions = 1,
        sets = 1,
        repetitionPaceSeconds = 6.0f,
        peakHoldRatio = 0.40f,
        isBilateral = false,
    )

    @Test
    fun bilateralExerciseCompletesLeftIntoThreeSecondSwitchSidesThenAdvancesToBreakAndCheckIn() = runTest {
        val fakeAudio = FakeAudioCueService()
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = fakeAudio,
            routineName = "Lower Back Recovery",
            exercises = listOf(bilateralFirstExercise, unilateralSecondExercise),
            breakDurationSeconds = 5,
        )

        // Rep 1 -> Rep 2 on Left Side
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        assertEquals(2, viewModel.state.value.currentRepetition)
        assertEquals(GuidedPlayerMode.ACTIVE, viewModel.state.value.mode)

        // Rep 2 completes Left Side -> enters 3s Switch Sides for Right Side
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        assertEquals(GuidedPlayerMode.SWITCH_SIDES, viewModel.state.value.mode)
        assertEquals(ExerciseSide.RIGHT, viewModel.state.value.currentSide)
        assertEquals(3, viewModel.state.value.remainingSwitchSidesSeconds)
        assertEquals(listOf(AudioCueKeys.SWITCH_SIDES), fakeAudio.playedCueKeys)

        // 3 ticks end Switch Sides -> Active Right Side
        repeat(3) { viewModel.onIntent(GuidedSessionPlayerIntent.TickTimerSecond) }
        assertEquals(GuidedPlayerMode.ACTIVE, viewModel.state.value.mode)
        assertEquals(ExerciseSide.RIGHT, viewModel.state.value.currentSide)

        // Complete Right Side (2 reps) -> enters Break preloading Exercise 2
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        assertEquals(GuidedPlayerMode.BREAK, viewModel.state.value.mode)
        assertEquals(1, viewModel.state.value.currentExerciseIndex)
        assertEquals(5, viewModel.state.value.remainingBreakSeconds)
        assertEquals(ExerciseSide.LEFT, viewModel.state.value.currentSide)

        // Skip Break -> complete last exercise -> emits PostSessionCheckIn
        viewModel.onIntent(GuidedSessionPlayerIntent.SkipBreak)
        assertEquals(GuidedPlayerMode.ACTIVE, viewModel.state.value.mode)

        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        val effect = viewModel.effects.first()
        assertEquals(
            GuidedSessionPlayerEffect.NavigateToPostSessionCheckIn("Lower Back Recovery"),
            effect,
        )
        assertEquals(
            listOf(AudioCueKeys.SWITCH_SIDES, AudioCueKeys.BREAK_START, AudioCueKeys.SESSION_COMPLETE),
            fakeAudio.playedCueKeys,
        )
    }

    @Test
    fun openingInfoSheetAutoPausesAndDismissingRestoresPreviousPlaybackState() {
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = FakeAudioCueService(),
            exercises = listOf(bilateralFirstExercise),
        )

        assertFalse(viewModel.state.value.isPaused)
        viewModel.onIntent(GuidedSessionPlayerIntent.OpenInfoSheet)
        assertTrue(viewModel.state.value.isPaused)
        assertTrue(viewModel.state.value.isInfoSheetVisible)

        viewModel.onIntent(GuidedSessionPlayerIntent.DismissInfoSheet)
        assertFalse(viewModel.state.value.isPaused)
        assertFalse(viewModel.state.value.isInfoSheetVisible)

        // When already paused before Info Sheet, dismissing keeps paused state
        viewModel.onIntent(GuidedSessionPlayerIntent.TogglePause)
        assertTrue(viewModel.state.value.isPaused)
        viewModel.onIntent(GuidedSessionPlayerIntent.OpenInfoSheet)
        viewModel.onIntent(GuidedSessionPlayerIntent.DismissInfoSheet)
        assertTrue(viewModel.state.value.isPaused)
    }

    @Test
    fun phaseChangesRespectMinimumOneSecondCalmInterval() {
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = FakeAudioCueService(),
            exercises = listOf(bilateralFirstExercise),
        )

        viewModel.onIntent(
            GuidedSessionPlayerIntent.UpdatePlaybackPhase(
                phase = ExercisePhase.HOLD,
                remainingHoldSeconds = 2,
                elapsedMilliseconds = 1_500L,
            ),
        )
        assertEquals(ExercisePhase.HOLD, viewModel.state.value.currentPhase)
        assertEquals(2, viewModel.state.value.remainingHoldSeconds)

        // Attempt rapid phase change after only 400ms -> throttled
        viewModel.onIntent(
            GuidedSessionPlayerIntent.UpdatePlaybackPhase(
                phase = ExercisePhase.RETURN,
                remainingHoldSeconds = 0,
                elapsedMilliseconds = 1_900L,
            ),
        )
        assertEquals(ExercisePhase.HOLD, viewModel.state.value.currentPhase)

        // After >= 1000ms -> allowed smoothly
        viewModel.onIntent(
            GuidedSessionPlayerIntent.UpdatePlaybackPhase(
                phase = ExercisePhase.RETURN,
                remainingHoldSeconds = 0,
                elapsedMilliseconds = 2_550L,
            ),
        )
        assertEquals(ExercisePhase.RETURN, viewModel.state.value.currentPhase)
    }

    @Test
    fun addBreakTimeExtendsRemainingAndTotalBreakDuration() {
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = FakeAudioCueService(),
            exercises = listOf(bilateralFirstExercise, unilateralSecondExercise),
            breakDurationSeconds = 5,
        )

        viewModel.onIntent(GuidedSessionPlayerIntent.NextExercise)
        assertEquals(GuidedPlayerMode.BREAK, viewModel.state.value.mode)
        assertEquals(5, viewModel.state.value.remainingBreakSeconds)

        viewModel.onIntent(GuidedSessionPlayerIntent.AddBreakTime)
        assertEquals(10, viewModel.state.value.remainingBreakSeconds)
        assertEquals(10, viewModel.state.value.totalBreakSeconds)
    }

    @Test
    fun previousExerciseAtFirstIndexStaysSafelyClamped() {
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = FakeAudioCueService(),
            exercises = listOf(bilateralFirstExercise, unilateralSecondExercise),
        )

        viewModel.onIntent(GuidedSessionPlayerIntent.PreviousExercise)
        assertEquals(0, viewModel.state.value.currentExerciseIndex)
        assertEquals(1, viewModel.state.value.currentRepetition)
    }

    @Test
    fun sessionAndExerciseDurationsAccountForBilateralSwitchSidesAndDefaultTenSecondBreaks() {
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = FakeAudioCueService(),
            exercises = listOf(bilateralFirstExercise, unilateralSecondExercise),
        )
        val initialState = viewModel.state.value

        // Default break is 10s
        assertEquals(10, initialState.breakDurationSeconds)
        // bilateralFirstExercise: 2*6s * 2 sides + 3s switch = 27s; unilateralSecond: 1*6s = 6s; 1 break = 10s -> 43s
        assertEquals(27, bilateralFirstExercise.totalDurationSeconds(10))
        assertEquals(6, unilateralSecondExercise.totalDurationSeconds(10))
        assertEquals(43, initialState.totalSessionDurationSeconds)
        assertEquals(43, initialState.remainingSessionSeconds(currentCycleProgress = 0f))
        assertEquals(0f, initialState.currentExerciseProgressFraction(currentCycleProgress = 0f), 0.001f)

        // Halfway through Rep 1 (3s elapsed) -> 40s remaining, 9s left on current side
        assertEquals(40, initialState.remainingSessionSeconds(currentCycleProgress = 0.5f))
        assertEquals(9f, initialState.remainingActiveSideSeconds(currentCycleProgress = 0.5f), 0.01f)

        // Final 3 seconds of active side on Rep 2 (halfway through 6s rep)
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        assertEquals(3f, viewModel.state.value.remainingActiveSideSeconds(currentCycleProgress = 0.5f), 0.01f)
    }

    @Test
    fun multiSetExerciseEntersBetweenSetsBreakBeforeAdvancingToNextExercise() {
        val multiSetExercise = unilateralSecondExercise.copy(
            identifier = "ex-multi-set",
            targetRepetitions = 2,
            sets = 2,
            repetitionPaceSeconds = 5.0f,
        )
        val viewModel = GuidedSessionPlayerViewModel(
            audioCueService = FakeAudioCueService(),
            exercises = listOf(multiSetExercise, unilateralSecondExercise),
            breakDurationSeconds = 10,
        )

        // multiSetExercise: 2 sets * 10s + 10s between-set break = 30s; second exercise = 6s; between-exercise break = 10s -> 46s
        assertEquals(30, multiSetExercise.totalDurationSeconds(10))
        assertEquals(46, viewModel.state.value.totalSessionDurationSeconds)

        // Complete Set 1 (2 reps) -> enters between-sets Break for Set 2 on same exercise
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)

        assertEquals(GuidedPlayerMode.BREAK, viewModel.state.value.mode)
        assertTrue(viewModel.state.value.isBetweenSetsBreak)
        assertTrue(viewModel.state.value.isBetweenSetsRest)
        assertFalse(viewModel.state.value.isUpNextBreak)
        assertEquals(0, viewModel.state.value.currentExerciseIndex)
        assertEquals(2, viewModel.state.value.currentSet)
        assertEquals(1, viewModel.state.value.currentRepetition)

        // Skip between-sets Break -> Active Set 2
        viewModel.onIntent(GuidedSessionPlayerIntent.SkipBreak)
        assertEquals(GuidedPlayerMode.ACTIVE, viewModel.state.value.mode)
        assertFalse(viewModel.state.value.isBetweenSetsBreak)
        assertFalse(viewModel.state.value.isBetweenSetsRest)
        assertFalse(viewModel.state.value.isUpNextBreak)
        assertEquals(2, viewModel.state.value.currentSet)

        // Complete Set 2 (2 reps) -> advances to Break for Exercise 2 with currentSet reset to 1
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)
        viewModel.onIntent(GuidedSessionPlayerIntent.CompleteRepetitionCycle)

        assertEquals(GuidedPlayerMode.BREAK, viewModel.state.value.mode)
        assertFalse(viewModel.state.value.isBetweenSetsBreak)
        assertFalse(viewModel.state.value.isBetweenSetsRest)
        assertTrue(viewModel.state.value.isUpNextBreak)
        assertEquals(1, viewModel.state.value.currentExerciseIndex)
        assertEquals(1, viewModel.state.value.currentSet)
    }
}
