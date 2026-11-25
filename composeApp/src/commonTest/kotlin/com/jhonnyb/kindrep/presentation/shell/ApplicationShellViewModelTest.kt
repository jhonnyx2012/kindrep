package com.jhonnyb.kindrep.presentation.shell

import com.jhonnyb.kindrep.data.repository.InMemoryOnboardingPreferencesRepository
import com.jhonnyb.kindrep.domain.service.AudioCueKeys
import com.jhonnyb.kindrep.fakes.FakeAudioCueService
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ApplicationShellViewModelTest {

    @Test
    fun firstLaunchInitializesWithUnacceptedDisclaimerOnTodayDestination() {
        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = InMemoryOnboardingPreferencesRepository(),
            audioCueService = FakeAudioCueService(),
        )

        assertFalse(viewModel.state.value.hasAcceptedDisclaimer)
        assertEquals(TopLevelDestination.TODAY, viewModel.state.value.selectedDestination)
        assertFalse(viewModel.state.value.isSafetyDialogVisible)
    }

    @Test
    fun returningUserWithAcceptedDisclaimerSkipsWelcomeGate() {
        val preferencesRepository = InMemoryOnboardingPreferencesRepository().apply {
            recordDisclaimerAcceptance()
        }

        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = preferencesRepository,
            audioCueService = FakeAudioCueService(),
        )

        assertTrue(viewModel.state.value.hasAcceptedDisclaimer)
    }

    @Test
    fun acceptingDisclaimerPersistsChoiceAndEmitsOnboardingNavigationEffect() = runTest {
        val preferencesRepository = InMemoryOnboardingPreferencesRepository()
        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = preferencesRepository,
            audioCueService = FakeAudioCueService(),
        )
        val emittedEffects = mutableListOf<ApplicationShellEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.effects.collect { emittedEffects += it }
        }

        viewModel.onIntent(ApplicationShellIntent.AcceptDisclaimer)

        assertTrue(preferencesRepository.hasAcceptedDisclaimer.value)
        assertTrue(viewModel.state.value.hasAcceptedDisclaimer)
        assertEquals(
            listOf<ApplicationShellEffect>(ApplicationShellEffect.NavigateToTodayAfterOnboarding),
            emittedEffects,
        )
    }

    @Test
    fun reselectingCurrentDestinationSuppressesDuplicateNavigationEffect() = runTest {
        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = InMemoryOnboardingPreferencesRepository(),
            audioCueService = FakeAudioCueService(),
        )
        val emittedEffects = mutableListOf<ApplicationShellEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.effects.collect { emittedEffects += it }
        }

        viewModel.onIntent(
            ApplicationShellIntent.SelectTopLevelDestination(TopLevelDestination.TODAY),
        )
        assertTrue(emittedEffects.isEmpty())

        viewModel.onIntent(
            ApplicationShellIntent.SelectTopLevelDestination(TopLevelDestination.ROUTINES),
        )
        viewModel.onIntent(
            ApplicationShellIntent.SelectTopLevelDestination(TopLevelDestination.ROUTINES),
        )

        assertEquals(TopLevelDestination.ROUTINES, viewModel.state.value.selectedDestination)
        assertEquals(
            listOf<ApplicationShellEffect>(
                ApplicationShellEffect.NavigateToTopLevelDestination(TopLevelDestination.ROUTINES),
            ),
            emittedEffects,
        )
    }

    @Test
    fun openingAndDismissingSafetyDialogUpdatesVisibilityWithoutEmittingNavigation() = runTest {
        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = InMemoryOnboardingPreferencesRepository(),
            audioCueService = FakeAudioCueService(),
        )
        val emittedEffects = mutableListOf<ApplicationShellEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.effects.collect { emittedEffects += it }
        }

        viewModel.onIntent(ApplicationShellIntent.OpenSafetyDialog)
        assertTrue(viewModel.state.value.isSafetyDialogVisible)

        viewModel.onIntent(ApplicationShellIntent.DismissSafetyDialog)
        assertFalse(viewModel.state.value.isSafetyDialogVisible)
        assertTrue(emittedEffects.isEmpty())
    }

    @Test
    fun startingSessionPlaysReadyCueAndEmitsGuidedPlayerNavigationForRoutine() = runTest {
        val audioCueService = FakeAudioCueService()
        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = InMemoryOnboardingPreferencesRepository(),
            audioCueService = audioCueService,
        )
        val emittedEffects = mutableListOf<ApplicationShellEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.effects.collect { emittedEffects += it }
        }

        viewModel.onIntent(ApplicationShellIntent.StartSession("shoulder-mobility"))

        assertEquals(listOf(AudioCueKeys.READY), audioCueService.playedCueKeys)
        assertEquals(
            listOf<ApplicationShellEffect>(
                ApplicationShellEffect.NavigateToGuidedSession("shoulder-mobility"),
            ),
            emittedEffects,
        )
    }
}
