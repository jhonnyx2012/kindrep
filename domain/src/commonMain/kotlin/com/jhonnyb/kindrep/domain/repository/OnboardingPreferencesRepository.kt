package com.jhonnyb.kindrep.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface OnboardingPreferencesRepository {
    val hasAcceptedDisclaimer: StateFlow<Boolean>
    fun recordDisclaimerAcceptance()
}
