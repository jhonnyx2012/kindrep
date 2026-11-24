package com.jhonnybarrios.kindrep.di

import com.jhonnybarrios.kindrep.platform.AudioCuePlayer
import com.jhonnybarrios.kindrep.platform.MediaResolver
import com.jhonnybarrios.kindrep.platform.createAudioCuePlayer
import com.jhonnybarrios.kindrep.platform.createMediaResolver
import com.jhonnybarrios.kindrep.presentation.AppShellViewModel
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
