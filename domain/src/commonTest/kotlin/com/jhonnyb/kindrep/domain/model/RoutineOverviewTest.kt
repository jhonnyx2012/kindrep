package com.jhonnyb.kindrep.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RoutineOverviewTest {

    @Test
    fun routineOverviewCapsVisibleExercisesAtFourAndComputesRemainingCount() {
        val routine = RoutineOverview.sampleLowerBackRecovery()

        assertEquals(RoutineOverview.MAXIMUM_VISIBLE_EXERCISES, routine.visibleExercises.size)
        assertEquals("1. Cat-Cow", routine.visibleExercises.first().numberedTitle)
        assertEquals(1, routine.hiddenExerciseCount)
        assertEquals("1 / 2", routine.dailyProgressBadge)
    }
}
