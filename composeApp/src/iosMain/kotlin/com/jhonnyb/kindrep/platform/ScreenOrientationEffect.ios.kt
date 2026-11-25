package com.jhonnyb.kindrep.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
actual fun ScreenOrientationEffect(landscape: Boolean) {
    DisposableEffect(landscape) {
        onDispose {}
    }
}
