package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.routine_metadata_summary
import com.jhonnyb.kindrep.generated.resources.today_hidden_exercises_more
import com.jhonnyb.kindrep.generated.resources.today_start_session
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun RoutineSummaryCard(
    routine: RoutineOverview,
    onStartSession: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = palette.surface,
        border = BorderStroke(1.dp, palette.border),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = palette.textPrimary,
                    )
                    Text(
                        text = stringResource(
                            Res.string.routine_metadata_summary,
                            routine.exercises.size,
                            routine.estimatedMinutes,
                            routine.breakDurationSeconds,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textMuted,
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(palette.secondarySoft)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Text(
                        text = routine.dailyProgressBadge,
                        style = MaterialTheme.typography.labelMedium,
                        color = palette.secondary,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                routine.visibleExercises.forEach { exercise ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = exercise.numberedTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = palette.textPrimary,
                        )
                        Text(
                            text = exercise.targetDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = palette.textMuted,
                        )
                    }
                }
                if (routine.hiddenExerciseCount > 0) {
                    Text(
                        text = stringResource(Res.string.today_hidden_exercises_more, routine.hiddenExerciseCount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textMuted,
                    )
                }
            }

            Button(
                onClick = { onStartSession(routine.identifier) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(
                    text = stringResource(Res.string.today_start_session),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun RoutineSummaryCardPreview() {
    KindrepTheme {
        RoutineSummaryCard(
            routine = RoutineOverview.sampleLowerBackRecovery(),
            onStartSession = {},
        )
    }
}
