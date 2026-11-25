package com.jhonnyb.kindrep.presentation.shell

import com.jhonnyb.kindrep.domain.model.RoutineOverview
import com.jhonnyb.kindrep.navigation.TopLevelDestination

data class ApplicationShellState(
    val hasAcceptedDisclaimer: Boolean = false,
    val selectedDestination: TopLevelDestination = TopLevelDestination.TODAY,
    val isSafetyDialogVisible: Boolean = false,
    val streakDayCount: Int = 5,
    val activeRoutine: RoutineOverview = RoutineOverview.sampleLowerBackRecovery(),
)
