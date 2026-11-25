package com.jhonnyb.kindrep

import androidx.compose.ui.graphics.Color
import com.jhonnyb.kindrep.data.repository.InMemoryOnboardingPreferencesRepository
import com.jhonnyb.kindrep.dependencyinjection.applicationModule
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.domain.service.AudioCueKeys
import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellViewModel
import com.jhonnyb.kindrep.ui.theme.SoftStoneD3Palette
import com.jhonnyb.kindrep.ui.theme.WarmEarthPalette
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
    fun dependencyGraphResolvesDomainContractsAndViewModel() {
        val koin = startKoin {
            modules(applicationModule)
        }.koin

        assertNotNull(koin.get<AudioCueService>())
        assertNotNull(koin.get<MediaAssetLocator>())
        assertNotNull(koin.get<ApplicationShellViewModel>())
    }

    @Test
    fun applicationShellViewModelManagesOnboardingAndNavigationState() {
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

        viewModel.acceptDisclaimer()
        viewModel.selectTopLevelDestination(TopLevelDestination.ROUTINES)
        viewModel.setSafetyDialogVisible(true)
        viewModel.triggerAudioCue(AudioCueKeys.SWITCH_SIDES)

        assertTrue(viewModel.state.value.hasAcceptedDisclaimer)
        assertEquals(TopLevelDestination.ROUTINES, viewModel.state.value.selectedDestination)
        assertTrue(viewModel.state.value.isSafetyDialogVisible)
        assertEquals(listOf(AudioCueKeys.SWITCH_SIDES), triggeredCues)
        assertFalse(viewModel.hasBundledVideo("bird_dog"))
    }

    @Test
    fun routineOverviewCapsVisibleExercisesAtFour() {
        val routine = RoutineOverview.sampleLowerBackRecovery()

        assertEquals(4, routine.visibleExercises.size)
        assertEquals(1, routine.hiddenExerciseCount)
        assertEquals("1 / 2", routine.dailyProgressBadge)
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
