package com.jhonnyb.kindrep.dependencyinjection

import com.jhonnyb.kindrep.domain.repository.OnboardingPreferencesRepository
import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerViewModel
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class DependencyInjectionGraphTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun applicationModuleResolvesDomainContractsAndPresentationViewModels() {
        val koin = startKoin {
            modules(applicationModule)
        }.koin

        assertNotNull(koin.get<OnboardingPreferencesRepository>())
        assertNotNull(koin.get<AudioCueService>())
        assertNotNull(koin.get<MediaAssetLocator>())
        assertNotNull(koin.get<ApplicationShellViewModel>())
        assertNotNull(koin.get<GuidedSessionPlayerViewModel>())
    }
}
