package com.jhonnyb.kindrep.presentation.shell

import androidx.lifecycle.ViewModel
import com.jhonnyb.kindrep.domain.repository.OnboardingPreferencesRepository
import com.jhonnyb.kindrep.domain.service.AudioCueKeys
import com.jhonnyb.kindrep.domain.service.AudioCueService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class ApplicationShellViewModel(
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository,
    private val audioCueService: AudioCueService,
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        ApplicationShellState(
            hasAcceptedDisclaimer = onboardingPreferencesRepository.hasAcceptedDisclaimer.value,
        ),
    )
    val state: StateFlow<ApplicationShellState> = mutableState.asStateFlow()

    private val effectChannel = Channel<ApplicationShellEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<ApplicationShellEffect> = effectChannel.receiveAsFlow()

    fun onIntent(intent: ApplicationShellIntent) {
        when (intent) {
            ApplicationShellIntent.AcceptDisclaimer -> handleAcceptDisclaimer()
            is ApplicationShellIntent.SelectTopLevelDestination -> handleSelectDestination(intent)
            ApplicationShellIntent.OpenSafetyDialog -> setSafetyDialogVisible(visible = true)
            ApplicationShellIntent.DismissSafetyDialog -> setSafetyDialogVisible(visible = false)
            is ApplicationShellIntent.StartSession -> handleStartSession(intent.routineIdentifier)
        }
    }

    private fun handleAcceptDisclaimer() {
        onboardingPreferencesRepository.recordDisclaimerAcceptance()
        mutableState.update { currentState ->
            currentState.copy(hasAcceptedDisclaimer = true)
        }
        effectChannel.trySend(ApplicationShellEffect.NavigateToTodayAfterOnboarding)
    }

    private fun handleSelectDestination(intent: ApplicationShellIntent.SelectTopLevelDestination) {
        val previousDestination = mutableState.value.selectedDestination
        mutableState.update { currentState ->
            currentState.copy(selectedDestination = intent.destination)
        }
        if (previousDestination != intent.destination) {
            effectChannel.trySend(
                ApplicationShellEffect.NavigateToTopLevelDestination(intent.destination),
            )
        }
    }

    private fun setSafetyDialogVisible(visible: Boolean) {
        mutableState.update { currentState ->
            currentState.copy(isSafetyDialogVisible = visible)
        }
    }

    private fun handleStartSession(routineIdentifier: String) {
        audioCueService.playCue(AudioCueKeys.READY)
        effectChannel.trySend(
            ApplicationShellEffect.NavigateToGuidedSession(routineIdentifier = routineIdentifier),
        )
    }
}
