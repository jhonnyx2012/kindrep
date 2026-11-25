package com.jhonnyb.kindrep.presentation.shell

import androidx.lifecycle.ViewModel
import com.jhonnyb.kindrep.domain.repository.OnboardingPreferencesRepository
import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ApplicationShellViewModel(
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository,
    private val audioCueService: AudioCueService,
    private val mediaAssetLocator: MediaAssetLocator,
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        ApplicationShellState(
            hasAcceptedDisclaimer = onboardingPreferencesRepository.hasAcceptedDisclaimer.value,
        ),
    )
    val state: StateFlow<ApplicationShellState> = mutableState.asStateFlow()

    fun acceptDisclaimer() {
        onboardingPreferencesRepository.recordDisclaimerAcceptance()
        mutableState.update { currentState ->
            currentState.copy(hasAcceptedDisclaimer = true)
        }
    }

    fun selectTopLevelDestination(destination: TopLevelDestination) {
        mutableState.update { currentState ->
            currentState.copy(selectedDestination = destination)
        }
    }

    fun setSafetyDialogVisible(visible: Boolean) {
        mutableState.update { currentState ->
            currentState.copy(isSafetyDialogVisible = visible)
        }
    }

    fun triggerAudioCue(cueKey: String = "ready") {
        audioCueService.playCue(cueKey)
    }

    fun hasBundledVideo(mediaKey: String): Boolean =
        mediaAssetLocator.resolveLocalVideoPath(mediaKey) != null
}
