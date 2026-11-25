package com.jhonnyb.kindrep.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RoutineOverviewTest {

    @Test
    fun routineBelowFourExerciseCapDisplaysAllExercisesWithZeroHiddenCount() {
        val routine = createRoutineWithExerciseCount(count = 3)

        assertEquals(3, routine.visibleExercises.size)
        assertEquals(0, routine.hiddenExerciseCount)
    }

    @Test
    fun routineAtExactFourExerciseBoundaryDisplaysAllExercisesWithZeroHiddenCount() {
        val routine = createRoutineWithExerciseCount(
            count = RoutineOverview.MAXIMUM_VISIBLE_EXERCISES,
        )

        assertEquals(RoutineOverview.MAXIMUM_VISIBLE_EXERCISES, routine.visibleExercises.size)
        assertEquals(0, routine.hiddenExerciseCount)
    }

    @Test
    fun routineExceedingFourExerciseCapTruncatesPreviewAndReportsRemainingCount() {
        val routine = createRoutineWithExerciseCount(count = 6)

        assertEquals(RoutineOverview.MAXIMUM_VISIBLE_EXERCISES, routine.visibleExercises.size)
        assertEquals(
            listOf("1. Exercise 1", "2. Exercise 2", "3. Exercise 3", "4. Exercise 4"),
            routine.visibleExercises.map { it.numberedTitle },
        )
        assertEquals(2, routine.hiddenExerciseCount)
    }

    @Test
    fun dailyProgressBadgeFormatsCompletedVersusPrescribedSessions() {
        val untouchedMorningPlan = createRoutineWithExerciseCount(
            count = 2,
            completedSessionsToday = 0,
            dailyTargetSessions = 3,
        )

        assertEquals("0 / 3", untouchedMorningPlan.dailyProgressBadge)
    }

    private fun createRoutineWithExerciseCount(
        count: Int,
        completedSessionsToday: Int = 1,
        dailyTargetSessions: Int = 2,
    ): RoutineOverview = RoutineOverview(
        identifier = "test-routine",
        name = "Test Recovery Plan",
        estimatedMinutes = 6,
        breakDurationSeconds = 5,
        completedSessionsToday = completedSessionsToday,
        dailyTargetSessions = dailyTargetSessions,
        exercises = (1..count).map { index ->
            ExerciseSummary(
                orderNumber = index,
                title = "Exercise $index",
                targetDescription = "10 reps",
            )
        },
    )
}
