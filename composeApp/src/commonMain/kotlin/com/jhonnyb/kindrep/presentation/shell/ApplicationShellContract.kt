package com.jhonnyb.kindrep.presentation.shell

import androidx.compose.runtime.Immutable
import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.navigation.TopLevelDestination

@Immutable
data class ApplicationShellState(
    val hasAcceptedDisclaimer: Boolean = false,
    val selectedDestination: TopLevelDestination = TopLevelDestination.TODAY,
    val isSafetyDialogVisible: Boolean = false,
    val streakDayCount: Int = 5,
    val activeRoutine: RoutineOverview = RoutineOverview.sampleLowerBackRecovery(),
)

sealed interface ApplicationShellIntent {
    data object AcceptDisclaimer : ApplicationShellIntent
    data class SelectTopLevelDestination(val destination: TopLevelDestination) : ApplicationShellIntent
    data object OpenSafetyDialog : ApplicationShellIntent
    data object DismissSafetyDialog : ApplicationShellIntent
    data class StartSession(val routineIdentifier: String) : ApplicationShellIntent
}

sealed interface ApplicationShellEffect {
    data object NavigateToTodayAfterOnboarding : ApplicationShellEffect
    data class NavigateToTopLevelDestination(val destination: TopLevelDestination) : ApplicationShellEffect
    data class NavigateToGuidedSession(val routineIdentifier: String) : ApplicationShellEffect
}
