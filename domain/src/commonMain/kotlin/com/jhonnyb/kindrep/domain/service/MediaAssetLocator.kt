package com.jhonnyb.kindrep.domain.service

interface MediaAssetLocator {
    fun resolveLocalVideoPath(mediaKey: String): String?
}
