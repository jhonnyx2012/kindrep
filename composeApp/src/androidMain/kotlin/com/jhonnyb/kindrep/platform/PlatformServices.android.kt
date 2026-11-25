package com.jhonnyb.kindrep.platform

import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator

private class AndroidAudioCueService : AudioCueService {
    override fun playCue(cueKey: String) = Unit

    override fun release() = Unit
}

private class AndroidMediaAssetLocator : MediaAssetLocator {
    override fun resolveLocalVideoPath(mediaKey: String): String? = null
}

actual fun createPlatformAudioCueService(): AudioCueService = AndroidAudioCueService()

actual fun createPlatformMediaAssetLocator(): MediaAssetLocator = AndroidMediaAssetLocator()
