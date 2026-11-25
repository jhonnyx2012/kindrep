package com.jhonnyb.kindrep.platform

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
actual fun PreferLandscapeOrientationEffect() {
    DisposableEffect(Unit) {
        val activity = AndroidActivityRegistry.currentActivity
        val previousOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = previousOrientation
        }
    }
}
