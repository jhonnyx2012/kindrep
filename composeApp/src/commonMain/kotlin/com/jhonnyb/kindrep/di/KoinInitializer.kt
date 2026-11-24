package com.jhonnyb.kindrep.di

import com.jhonnyb.kindrep.platform.AudioCuePlayer
import com.jhonnyb.kindrep.platform.MediaResolver
import com.jhonnyb.kindrep.platform.createAudioCuePlayer
import com.jhonnyb.kindrep.platform.createMediaResolver
import com.jhonnyb.kindrep.presentation.AppShellViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single<AudioCuePlayer> { createAudioCuePlayer() }
    single<MediaResolver> { createMediaResolver() }
    viewModel { AppShellViewModel(get(), get()) }
}

private var koinRef: KoinApplication? = null

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
    koinRef?.let { return it }
    val app = startKoin {
        appDeclaration()
        modules(appModule)
    }
    koinRef = app
    return app
}
