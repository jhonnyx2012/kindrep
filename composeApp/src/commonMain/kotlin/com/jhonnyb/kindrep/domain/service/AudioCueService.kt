package com.jhonnyb.kindrep.domain.service

interface AudioCueService {
    fun playCue(cueKey: String)
    fun release()
}
