package com.jhonnyb.kindrep

import androidx.compose.ui.graphics.Color
import com.jhonnyb.kindrep.data.repository.InMemoryOnboardingPreferencesRepository
import com.jhonnyb.kindrep.dependencyinjection.applicationModule
import com.jhonnyb.kindrep.domain.service.AudioCueKeys
import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellEffect
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellIntent
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellViewModel
import com.jhonnyb.kindrep.ui.theme.SoftStoneD3Palette
import com.jhonnyb.kindrep.ui.theme.WarmEarthPalette
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KindrepArchitectureTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun dependencyGraphResolvesMultiModuleContractsAndViewModel() {
        val koin = startKoin {
            modules(applicationModule)
        }.koin

        assertNotNull(koin.get<AudioCueService>())
        assertNotNull(koin.get<MediaAssetLocator>())
        assertNotNull(koin.get<ApplicationShellViewModel>())
    }

    @Test
    fun applicationShellViewModelProcessesIntentsAndEmitsEffectsUnidirectionally() = runTest {
        val triggeredCues = mutableListOf<String>()
        val fakeAudioCueService = object : AudioCueService {
            override fun playCue(cueKey: String) {
                triggeredCues += cueKey
            }

            override fun release() = Unit
        }
        val fakeMediaAssetLocator = object : MediaAssetLocator {
            override fun resolveLocalVideoPath(mediaKey: String): String? = null
        }

        val viewModel = ApplicationShellViewModel(
            onboardingPreferencesRepository = InMemoryOnboardingPreferencesRepository(),
            audioCueService = fakeAudioCueService,
            mediaAssetLocator = fakeMediaAssetLocator,
        )

        assertFalse(viewModel.state.value.hasAcceptedDisclaimer)
        assertEquals(TopLevelDestination.TODAY, viewModel.state.value.selectedDestination)

        viewModel.onIntent(ApplicationShellIntent.AcceptDisclaimer)
        assertTrue(viewModel.state.value.hasAcceptedDisclaimer)
        assertEquals(
            ApplicationShellEffect.NavigateToTodayAfterOnboarding,
            viewModel.effects.first(),
        )

        viewModel.onIntent(
            ApplicationShellIntent.SelectTopLevelDestination(TopLevelDestination.ROUTINES),
        )
        assertEquals(TopLevelDestination.ROUTINES, viewModel.state.value.selectedDestination)
        assertEquals(
            ApplicationShellEffect.NavigateToTopLevelDestination(TopLevelDestination.ROUTINES),
            viewModel.effects.first(),
        )

        viewModel.onIntent(ApplicationShellIntent.OpenSafetyDialog)
        assertTrue(viewModel.state.value.isSafetyDialogVisible)

        viewModel.onIntent(ApplicationShellIntent.DismissSafetyDialog)
        assertFalse(viewModel.state.value.isSafetyDialogVisible)

        viewModel.onIntent(ApplicationShellIntent.StartSession("lower-back-recovery"))
        assertEquals(listOf(AudioCueKeys.READY), triggeredCues)
        assertEquals(
            ApplicationShellEffect.NavigateToGuidedSession("lower-back-recovery"),
            viewModel.effects.first(),
        )
    }

    @Test
    fun themePalettesMatchDesignTokens() {
        assertEquals(Color(0xFFF7F5F0), WarmEarthPalette.canvas)
        assertEquals(Color(0xFF5B7B6A), WarmEarthPalette.primary)
        assertEquals(Color(0xFFC87D55), WarmEarthPalette.secondary)

        assertEquals(Color(0xFF212624), SoftStoneD3Palette.canvas)
        assertEquals(Color(0xFF313835), SoftStoneD3Palette.surface)
        assertEquals(Color(0xFF4D5752), SoftStoneD3Palette.border)
        assertEquals(Color(0xFFF2EEE6), SoftStoneD3Palette.mascotBody)
        assertEquals(Color(0xFF648977), SoftStoneD3Palette.mascotStroke)
    }
}
