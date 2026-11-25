package com.jhonnyb.kindrep.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jhonnyb.kindrep.ui.components.ExerciseFigurePlaceholder

@Composable
actual fun LoopingExerciseVideo(
    mediaKey: String,
    isPlaying: Boolean,
    modifier: Modifier,
) {
    ExerciseFigurePlaceholder(modifier = modifier)
}
