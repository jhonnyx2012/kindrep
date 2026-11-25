package com.jhonnyb.kindrep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jhonnyb.kindrep.navigation.KindrepDestination
import com.jhonnyb.kindrep.navigation.KindrepNavigationHost
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellEffect
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellIntent
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellViewModel
import com.jhonnyb.kindrep.ui.dialogs.SafetyNoticeDialog
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun KindrepRoot() {
    KindrepTheme {
        val shellViewModel: ApplicationShellViewModel = koinViewModel()
        val shellState by shellViewModel.state.collectAsStateWithLifecycle()
        val navigationController = rememberNavController()
        val palette = LocalKindrepPalette.current

        ObserveShellEffects(
            shellViewModel = shellViewModel,
            navigationController = navigationController,
        )

        if (shellState.isSafetyDialogVisible) {
            SafetyNoticeDialog(
                onDismiss = {
                    shellViewModel.onIntent(ApplicationShellIntent.DismissSafetyDialog)
                },
            )
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = palette.canvas,
        ) {
            KindrepNavigationHost(
                navigationController = navigationController,
                shellState = shellState,
                onShellIntent = shellViewModel::onIntent,
            )
        }
    }
}

@Composable
private fun ObserveShellEffects(
    shellViewModel: ApplicationShellViewModel,
    navigationController: NavHostController,
) {
    LaunchedEffect(shellViewModel, navigationController) {
        shellViewModel.effects.collect { effect ->
            when (effect) {
                ApplicationShellEffect.NavigateToTodayAfterOnboarding -> {
                    navigationController.navigate(KindrepDestination.Today) {
                        popUpTo(KindrepDestination.Welcome) { inclusive = true }
                    }
                }

                is ApplicationShellEffect.NavigateToTopLevelDestination -> {
                    val targetRoute = when (effect.destination) {
                        TopLevelDestination.TODAY -> KindrepDestination.Today
                        TopLevelDestination.ROUTINES -> KindrepDestination.Routines
                        TopLevelDestination.PROGRESS -> KindrepDestination.Progress
                    }
                    navigationController.navigate(targetRoute) {
                        launchSingleTop = true
                    }
                }

                is ApplicationShellEffect.NavigateToGuidedSession -> {
                    navigationController.navigate(
                        KindrepDestination.GuidedSessionPlayer(
                            routineIdentifier = effect.routineIdentifier,
                        ),
                    )
                }
            }
        }
    }
}
