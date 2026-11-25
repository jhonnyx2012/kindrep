package com.jhonnyb.kindrep.platform

import com.jhonnyb.kindrep.domain.service.AudioCueService
import com.jhonnyb.kindrep.domain.service.MediaAssetLocator

expect fun createPlatformAudioCueService(): AudioCueService

expect fun createPlatformMediaAssetLocator(): MediaAssetLocator
