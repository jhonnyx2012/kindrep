package com.jhonnyb.kindrep.data.repository

import com.jhonnyb.kindrep.domain.repository.OnboardingPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryOnboardingPreferencesRepository : OnboardingPreferencesRepository {
    private val acceptedDisclaimerState = MutableStateFlow(false)

    override val hasAcceptedDisclaimer: StateFlow<Boolean> =
        acceptedDisclaimerState.asStateFlow()

    override fun recordDisclaimerAcceptance() {
        acceptedDisclaimerState.value = true
    }
}
