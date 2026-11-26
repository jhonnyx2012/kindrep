package com.jhonnyb.kindrep.ui.animation

import com.jhonnyb.kindrep.domain.model.ExerciseScaleGroup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExerciseChoreographyCatalogTest {

    @Test
    fun allSampleSessionExercisesResolveToMatchingSpecsInCatalog() {
        val sampleSession = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises()
        assertEquals(13, sampleSession.size)
        assertEquals(
            listOf(
                ExerciseMediaKeys.CAT_COW,
                ExerciseMediaKeys.CHILDS_POSE,
                ExerciseMediaKeys.MCGILL_CURL_UP,
                ExerciseMediaKeys.GLUTE_BRIDGE,
                ExerciseMediaKeys.HAMSTRING_BRIDGE,
                ExerciseMediaKeys.LYING_LEG_CURL,
                ExerciseMediaKeys.SINGLE_LEG_HIP_HINGE,
                ExerciseMediaKeys.SOLEUS_HEEL_RAISE,
                ExerciseMediaKeys.STANDING_CALF_RAISE,
                ExerciseMediaKeys.WALL_CALF_STRETCH,
                ExerciseMediaKeys.DEADLIFT_HIP_HINGE,
                ExerciseMediaKeys.PIRIFORMIS_STRETCH,
                ExerciseMediaKeys.SEATED_GLUTE_RELEASE,
            ),
            sampleSession.map { it.mediaKey },
        )

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
    fun allCatalogSpecsResolveToDedicatedCopyResourcesAndBounds() {
        assertEquals(29, ExerciseChoreographyCatalog.allSpecs.size)
        val birdDogTitle = ExerciseCopyCatalog.resolve(ExerciseMediaKeys.BIRD_DOG).title

        ExerciseChoreographyCatalog.allSpecs.forEach { spec ->
            val resolvedSpec = ExerciseChoreographyCatalog.resolveSpec(spec.mediaKey)
            val resolvedBounds = ExerciseChoreographyCatalog.resolveBounds(spec.mediaKey)
            val resolvedCopy = ExerciseCopyCatalog.resolve(spec.mediaKey)

            assertEquals(spec.mediaKey, resolvedSpec.mediaKey)
            assertTrue(resolvedBounds.width > 40f && resolvedBounds.height > 40f)
            if (spec.mediaKey != ExerciseMediaKeys.BIRD_DOG) {
                assertTrue(resolvedCopy.title != birdDogTitle)
            }
        }
    }

    @Test
    fun floorAndVerticalScaleGroupsHavePositiveUnifiedDimensions() {
        val floorGroup = ExerciseChoreographyCatalog.unifiedGroupDimensions[ExerciseScaleGroup.FLOOR]
        val verticalGroup = ExerciseChoreographyCatalog.unifiedGroupDimensions[ExerciseScaleGroup.VERTICAL]

        assertTrue(floorGroup != null && floorGroup.first > 100f && floorGroup.second > 60f)
        assertTrue(verticalGroup != null && verticalGroup.first > 80f && verticalGroup.second > 100f)
    }
}
