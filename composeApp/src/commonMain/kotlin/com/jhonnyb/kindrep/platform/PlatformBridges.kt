package com.jhonnyb.kindrep.platform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette

/**
 * 1. Looping exercise video player (Media3 ExoPlayer on Android / AVPlayerLooper on iOS),
 *    falling back cleanly to the neutral-gray + terracotta placeholder figure while videos are produced.
 */
@Composable
expect fun LoopingExerciseVideo(
    mediaKey: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
)

/**
 * 2. Audio cues & countdown chimes with transient audio ducking (never pauses Spotify/podcasts).
 */
interface AudioCuePlayer {
    fun playChime(cueKey: String)
    fun release()
}

expect fun createAudioCuePlayer(): AudioCuePlayer

/**
 * 3. Keeps the device screen awake while a guided recovery session is active.
 */
@Composable
expect fun KeepScreenAwake(enabled: Boolean)

/**
 * 4. Resolves a seed catalog `mediaKey` to a bundled local asset URI (or null when using placeholder art).
 */
interface MediaResolver {
    fun resolveLocalVideoUri(mediaKey: String): String?
}

expect fun createMediaResolver(): MediaResolver

/**
 * 5. Locks Guided Player (`04A–04E`) to Full-Screen Landscape and restores Portrait on exit.
 */
@Composable
expect fun LockScreenOrientation(landscape: Boolean)

/**
 * Neutral calm gray (`#94989B` Light / `#D0D4D6` Dark) + terracotta active-limb figure placeholder.
 */
@Composable
fun ExerciseFigurePlaceholder(
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.surfaceAlt),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(180.dp, 120.dp)) {
            val w = size.width
            val h = size.height
            // Mat line
            drawLine(
                color = palette.matFill,
                start = Offset(w * 0.08f, h * 0.84f),
                end = Offset(w * 0.92f, h * 0.84f),
                strokeWidth = 10f,
                cap = StrokeCap.Round,
            )
            // Neutral gray torso + supporting limbs (Bird-Dog form)
            drawCircle(color = palette.figureBody, radius = 14f, center = Offset(w * 0.66f, h * 0.40f))
            drawLine(palette.figureBody, Offset(w * 0.38f, h * 0.48f), Offset(w * 0.60f, h * 0.46f), 16f, StrokeCap.Round)
            drawLine(palette.figureBody, Offset(w * 0.40f, h * 0.48f), Offset(w * 0.38f, h * 0.80f), 12f, StrokeCap.Round)
            drawLine(palette.figureBody, Offset(w * 0.58f, h * 0.48f), Offset(w * 0.60f, h * 0.80f), 12f, StrokeCap.Round)
            // Terracotta active extended limbs
            drawLine(palette.secondary, Offset(w * 0.60f, h * 0.45f), Offset(w * 0.86f, h * 0.38f), 11f, StrokeCap.Round)
            drawLine(palette.secondary, Offset(w * 0.38f, h * 0.48f), Offset(w * 0.14f, h * 0.42f), 11f, StrokeCap.Round)
        }
    }
}
