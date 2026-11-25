package com.jhonnyb.kindrep.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun LoopingExerciseVideo(
    mediaKey: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
)
