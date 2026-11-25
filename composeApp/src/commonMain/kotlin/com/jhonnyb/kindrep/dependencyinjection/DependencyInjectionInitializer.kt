package com.jhonnyb.kindrep.dependencyinjection

import com.jhonnyb.kindrep.data.repository.InMemoryOnboardingPreferencesRepository
import com.jhonnyb.kindrep.domain.repository.OnboardingPreferencesRepository
import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator
import com.jhonnyb.kindrep.platform.createPlatformAudioCueService
import com.jhonnyb.kindrep.platform.createPlatformMediaAssetLocator
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val applicationModule = module {
    single<OnboardingPreferencesRepository> { InMemoryOnboardingPreferencesRepository() }
    single<AudioCueService> { createPlatformAudioCueService() }
    single<MediaAssetLocator> { createPlatformMediaAssetLocator() }
    viewModel { ApplicationShellViewModel(get(), get(), get()) }
}

private var activeKoinApplication: KoinApplication? = null

fun initializeDependencyInjection(
    applicationConfiguration: KoinAppDeclaration = {},
): KoinApplication {
    activeKoinApplication?.let { return it }
    val createdApplication = startKoin {
        applicationConfiguration()
        modules(applicationModule)
    }
    activeKoinApplication = createdApplication
    return createdApplication
}
