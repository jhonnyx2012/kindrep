package com.jhonnyb.kindrep.ui.screens.today

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.app_name
import com.jhonnyb.kindrep.generated.resources.today_greeting
import com.jhonnyb.kindrep.generated.resources.today_safety_button_label
import com.jhonnyb.kindrep.generated.resources.today_streak_badge
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.ui.components.CompanionStreakCard
import com.jhonnyb.kindrep.ui.components.MainNavigationScaffold
import com.jhonnyb.kindrep.ui.components.RoutineSummaryCard
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun TodayHomeScreen(
    streakDayCount: Int,
    routine: RoutineOverview,
    onOpenSafetyNotice: () -> Unit,
    onStartSession: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TodayHeaderRow(
            streakDayCount = streakDayCount,
            onOpenSafetyNotice = onOpenSafetyNotice,
        )
        CompanionStreakCard()
        RoutineSummaryCard(
            routine = routine,
            onStartSession = onStartSession,
        )
    }
}

@Composable
private fun TodayHeaderRow(
    streakDayCount: Int,
    onOpenSafetyNotice: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TodayGreetingTitleBlock()
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StreakBadge(streakDayCount = streakDayCount)
            SafetyInformationButton(onClick = onOpenSafetyNotice)
        }
    }
}

@Composable
private fun TodayGreetingTitleBlock() {
    val palette = LocalKindrepPalette.current

    Column {
        Text(
            text = stringResource(Res.string.today_greeting),
            style = MaterialTheme.typography.bodyMedium,
            color = palette.textMuted,
        )
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
        )
    }
}

@Composable
private fun StreakBadge(streakDayCount: Int) {
    val palette = LocalKindrepPalette.current

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(9.dp))
            .background(palette.secondarySoft)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = stringResource(Res.string.today_streak_badge, streakDayCount),
            style = MaterialTheme.typography.labelMedium,
            color = palette.secondary,
        )
    }
}

@Composable
private fun SafetyInformationButton(onClick: () -> Unit) {
    val palette = LocalKindrepPalette.current

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(palette.surface)
            .border(1.dp, palette.border, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.today_safety_button_label),
            style = MaterialTheme.typography.labelMedium,
            color = palette.textPrimary,
        )
    }
}

@Preview(widthDp = 402, heightDp = 874)
@Composable
private fun TodayHomeScreenPreview() {
    KindrepTheme {
        MainNavigationScaffold(
            selectedDestination = TopLevelDestination.TODAY,
            onSelectDestination = {},
        ) {
            TodayHomeScreen(
                streakDayCount = 5,
                routine = RoutineOverview.sampleLowerBackRecovery(),
                onOpenSafetyNotice = {},
                onStartSession = {},
            )
        }
    }
}
