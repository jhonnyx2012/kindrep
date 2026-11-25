package com.jhonnyb.kindrep.data.repository

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InMemoryOnboardingPreferencesRepositoryTest {

    @Test
    fun disclaimerAcceptanceTransitionsFromFalseToTrue() {
        val repository = InMemoryOnboardingPreferencesRepository()

        assertFalse(repository.hasAcceptedDisclaimer.value)

        repository.recordDisclaimerAcceptance()

        assertTrue(repository.hasAcceptedDisclaimer.value)
    }
}
