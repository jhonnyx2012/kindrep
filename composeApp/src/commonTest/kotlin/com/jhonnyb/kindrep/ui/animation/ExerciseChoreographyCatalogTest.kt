package com.jhonnyb.kindrep.ui.animation

import com.jhonnyb.kindrep.domain.model.ExerciseScaleGroup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExerciseChoreographyCatalogTest {

    @Test
    fun allSampleSessionExercisesResolveToMatchingSpecsInCatalog() {
        val sampleSession = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises()
        assertEquals(9, sampleSession.size)

        sampleSession.forEach { exercise ->
            val resolved = ExerciseChoreographyCatalog.resolveSpec(exercise.mediaKey)
            assertEquals(exercise.mediaKey, resolved.mediaKey)
        }
    }

    @Test
    fun unknownMediaKeyFallsBackSafelyToBirdDog() {
        val fallback = ExerciseChoreographyCatalog.resolveSpec("non-existent-exercise")
        assertEquals(ExerciseMediaKeys.BIRD_DOG, fallback.mediaKey)
    }

    @Test
    fun floorAndVerticalScaleGroupsHavePositiveUnifiedDimensions() {
        val floorGroup = ExerciseChoreographyCatalog.unifiedGroupDimensions[ExerciseScaleGroup.FLOOR]
        val verticalGroup = ExerciseChoreographyCatalog.unifiedGroupDimensions[ExerciseScaleGroup.VERTICAL]

        assertTrue(floorGroup != null && floorGroup.first > 100f && floorGroup.second > 60f)
        assertTrue(verticalGroup != null && verticalGroup.first > 80f && verticalGroup.second > 100f)
    }
}
