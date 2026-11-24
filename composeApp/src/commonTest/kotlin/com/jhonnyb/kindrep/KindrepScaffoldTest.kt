package com.jhonnyb.kindrep

import androidx.compose.ui.graphics.Color
import com.jhonnyb.kindrep.di.appModule
import com.jhonnyb.kindrep.navigation.TopLevelTab
import com.jhonnyb.kindrep.platform.AudioCuePlayer
import com.jhonnyb.kindrep.platform.MediaResolver
import com.jhonnyb.kindrep.presentation.AppShellViewModel
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

class KindrepScaffoldTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun koinGraphResolvesCoreScaffoldDependencies() {
        val koin = startKoin {
            modules(appModule)
        }.koin

        assertNotNull(koin.get<AudioCuePlayer>())
        assertNotNull(koin.get<MediaResolver>())
        assertNotNull(koin.get<AppShellViewModel>())
    }

    @Test
    fun appShellViewModelHandlesOnboardingAndTabTransitions() {
        val playedCues = mutableListOf<String>()
        val fakeAudio = object : AudioCuePlayer {
            override fun playChime(cueKey: String) {
                playedCues += cueKey
            }
            override fun release() = Unit
        }
        val fakeMedia = object : MediaResolver {
            override fun resolveLocalVideoUri(mediaKey: String): String? = null
        }

        val vm = AppShellViewModel(fakeAudio, fakeMedia)
        assertFalse(vm.uiState.value.hasAcceptedDisclaimer)
        assertEquals(TopLevelTab.TODAY, vm.uiState.value.selectedTab)

        vm.acceptDisclaimer()
        vm.selectTab(TopLevelTab.ROUTINES)
        vm.setSafetyModalVisible(true)
        vm.previewAudioCue("switch_sides")

        assertTrue(vm.uiState.value.hasAcceptedDisclaimer)
        assertEquals(TopLevelTab.ROUTINES, vm.uiState.value.selectedTab)
        assertTrue(vm.uiState.value.isSafetyModalVisible)
        assertEquals(listOf("switch_sides"), playedCues)
        assertFalse(vm.hasBundledVideo("bird_dog"))
    }

    @Test
    fun lockedThemePalettesMatchDesignSystemTokens() {
        assertEquals(Color(0xFFF7F5F0), WarmEarthPalette.canvas)
        assertEquals(Color(0xFF5B7B6A), WarmEarthPalette.primary)
        assertEquals(Color(0xFFC87D55), WarmEarthPalette.secondary)

        // Soft Stone D3 + Option C1 dark mascot tokens
        assertEquals(Color(0xFF212624), SoftStoneD3Palette.canvas)
        assertEquals(Color(0xFF313835), SoftStoneD3Palette.surface)
        assertEquals(Color(0xFF4D5752), SoftStoneD3Palette.border)
        assertEquals(Color(0xFFF2EEE6), SoftStoneD3Palette.mascotBody)
        assertEquals(Color(0xFF648977), SoftStoneD3Palette.mascotStroke)
    }
}
