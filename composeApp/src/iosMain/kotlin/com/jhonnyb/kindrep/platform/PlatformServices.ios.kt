package com.jhonnyb.kindrep.platform

import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator

private class AppleAudioCueService : AudioCueService {
    override fun playCue(cueKey: String) = Unit

    override fun release() = Unit
}

private class AppleMediaAssetLocator : MediaAssetLocator {
    override fun resolveLocalVideoPath(mediaKey: String): String? = null
}

actual fun createPlatformAudioCueService(): AudioCueService = AppleAudioCueService()

actual fun createPlatformMediaAssetLocator(): MediaAssetLocator = AppleMediaAssetLocator()
