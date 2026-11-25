package com.jhonnyb.kindrep.data.repository

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InMemoryOnboardingPreferencesRepositoryTest {

    @Test
    fun newInstallRequiresSafetyDisclaimerAcceptanceByDefault() {
        val repository = InMemoryOnboardingPreferencesRepository()

        assertFalse(repository.hasAcceptedDisclaimer.value)
    }

    @Test
    fun recordingDisclaimerAcceptancePersistsIdempotentlyAcrossReads() {
        val repository = InMemoryOnboardingPreferencesRepository()

        repository.recordDisclaimerAcceptance()
        repository.recordDisclaimerAcceptance()

        assertTrue(repository.hasAcceptedDisclaimer.value)
    }
}
