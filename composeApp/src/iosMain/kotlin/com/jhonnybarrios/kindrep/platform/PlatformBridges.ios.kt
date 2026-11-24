package com.jhonnybarrios.kindrep.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import platform.UIKit.UIApplication

@Composable
actual fun LoopingExerciseVideo(
    mediaKey: String,
    isPlaying: Boolean,
    modifier: Modifier,
) {
    ExerciseFigurePlaceholder(modifier = modifier)
}

private class IosAudioCuePlayer : AudioCuePlayer {
    override fun playChime(cueKey: String) {
        // Week 2: AVAudioSession .playback with [.mixWithOthers, .duckOthers]
    }

    override fun release() = Unit
}

actual fun createAudioCuePlayer(): AudioCuePlayer = IosAudioCuePlayer()

@Composable
actual fun KeepScreenAwake(enabled: Boolean) {
    DisposableEffect(enabled) {
        UIApplication.sharedApplication.idleTimerDisabled = enabled
        onDispose {
            UIApplication.sharedApplication.idleTimerDisabled = false
        }
    }
}

private class IosMediaResolver : MediaResolver {
    override fun resolveLocalVideoUri(mediaKey: String): String? = null
}

actual fun createMediaResolver(): MediaResolver = IosMediaResolver()

@Composable
actual fun LockScreenOrientation(landscape: Boolean) {
    DisposableEffect(landscape) {
        // Week 2: UIWindowSceneGeometryPreferencesIOS landscape/portrait mask request
        onDispose {}
    }
}
