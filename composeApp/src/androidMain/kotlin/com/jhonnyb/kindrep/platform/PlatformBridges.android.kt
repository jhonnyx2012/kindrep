package com.jhonnyb.kindrep.platform

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier

object AndroidActivityHolder {
    var currentActivity: Activity? = null
}

@Composable
actual fun LoopingExerciseVideo(
    mediaKey: String,
    isPlaying: Boolean,
    modifier: Modifier,
) {
    ExerciseFigurePlaceholder(modifier = modifier)
}

private class AndroidAudioCuePlayer : AudioCuePlayer {
    override fun playChime(cueKey: String) {
        // Week 2: SoundPool / MediaPlayer with AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
    }

    override fun release() = Unit
}

actual fun createAudioCuePlayer(): AudioCuePlayer = AndroidAudioCuePlayer()

@Composable
actual fun KeepScreenAwake(enabled: Boolean) {
    DisposableEffect(enabled) {
        val window = AndroidActivityHolder.currentActivity?.window
        if (enabled) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

private class AndroidMediaResolver : MediaResolver {
    override fun resolveLocalVideoUri(mediaKey: String): String? = null
}

actual fun createMediaResolver(): MediaResolver = AndroidMediaResolver()

@Composable
actual fun LockScreenOrientation(landscape: Boolean) {
    DisposableEffect(landscape) {
        val activity = AndroidActivityHolder.currentActivity
        activity?.requestedOrientation = if (landscape) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
