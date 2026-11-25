package com.jhonnyb.kindrep.fakes

import com.jhonnyb.kindrep.domain.service.AudioCueService

class FakeAudioCueService : AudioCueService {
    private val recordedCueKeys = mutableListOf<String>()

    val playedCueKeys: List<String>
        get() = recordedCueKeys.toList()

    var isReleased: Boolean = false
        private set

    override fun playCue(cueKey: String) {
        recordedCueKeys += cueKey
    }

    override fun release() {
        isReleased = true
    }
}
