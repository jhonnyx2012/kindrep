package com.jhonnyb.kindrep.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.player_between_sets_rest_subtitle
import com.jhonnyb.kindrep.generated.resources.player_between_sets_rest_title_format
import com.jhonnyb.kindrep.generated.resources.player_between_sets_start_action_format
import com.jhonnyb.kindrep.generated.resources.player_break_add_five_seconds
import com.jhonnyb.kindrep.generated.resources.player_break_seconds_format
import com.jhonnyb.kindrep.presentation.player.GuidedPlayerMode
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerIntent
import com.jhonnyb.kindrep.presentation.player.GuidedSessionPlayerState
import com.jhonnyb.kindrep.ui.animation.ExerciseChoreographyCatalog
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

private val BetweenSetsRestRingStroke = Stroke(width = 4.0f, cap = StrokeCap.Round)

@Composable
fun BetweenSetsRestBottomStrip(
    state: GuidedSessionPlayerState,
    onIntent: (GuidedSessionPlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current
    val animatedProgress by animateFloatAsState(
        targetValue = state.breakProgressFraction,
        animationSpec = tween(420, easing = LinearOutSlowInEasing),
        label = "BetweenSetsRestRingProgress",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(palette.canvas)
            .border(1.dp, palette.border, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = palette.matFill,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = BetweenSetsRestRingStroke,
                    )
                    drawArc(
                        color = palette.primary,
                        startAngle = -90f,
                        sweepAngle = 360f * (1f - animatedProgress),
                        useCenter = false,
                        style = BetweenSetsRestRingStroke,
                    )
                }
                Text(
                    text = stringResource(
                        Res.string.player_break_seconds_format,
                        state.remainingBreakSeconds,
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFeatureSettings = "tnum",
                    ),
                    color = palette.primary,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = stringResource(
                        Res.string.player_between_sets_rest_title_format,
                        state.currentSet,
                        state.currentExercise.sets,
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFeatureSettings = "tnum",
                    ),
                    color = palette.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(Res.string.player_between_sets_rest_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = { onIntent(GuidedSessionPlayerIntent.AddBreakTime) },
                shape = RoundedCornerShape(11.dp),
                border = BorderStroke(1.dp, palette.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = palette.surface,
                    contentColor = palette.textPrimary,
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(40.dp),
            ) {
                Text(
                    text = stringResource(Res.string.player_break_add_five_seconds),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                )
            }
            Button(
                onClick = { onIntent(GuidedSessionPlayerIntent.SkipBreak) },
                shape = RoundedCornerShape(11.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                modifier = Modifier.height(40.dp),
            ) {
                Text(
                    text = stringResource(
                        Res.string.player_between_sets_start_action_format,
                        state.currentSet,
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Preview(widthDp = 874, heightDp = 120, showBackground = true)
@Composable
private fun BetweenSetsRestBottomStripPreview() {
    KindrepTheme {
        BetweenSetsRestBottomStrip(
            state = GuidedSessionPlayerState(
                routineName = RoutineOverview.sampleLowerBackRecovery().name,
                exercises = ExerciseChoreographyCatalog.sampleLowerBackSessionExercises(),
                currentExerciseIndex = 3,
                mode = GuidedPlayerMode.BREAK,
                currentSet = 2,
                isBetweenSetsBreak = true,
                remainingBreakSeconds = 8,
                totalBreakSeconds = 10,
            ),
            onIntent = {},
        )
    }
}
