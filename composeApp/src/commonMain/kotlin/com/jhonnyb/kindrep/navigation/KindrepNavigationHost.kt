package com.jhonnyb.kindrep.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.exercise_picker_subtitle
import com.jhonnyb.kindrep.generated.resources.exercise_picker_title
import com.jhonnyb.kindrep.generated.resources.progress_subtitle
import com.jhonnyb.kindrep.generated.resources.progress_title
import com.jhonnyb.kindrep.generated.resources.routine_editor_edit_title
import com.jhonnyb.kindrep.generated.resources.routine_editor_new_title
import com.jhonnyb.kindrep.generated.resources.routine_editor_subtitle
import com.jhonnyb.kindrep.generated.resources.routines_create_action
import com.jhonnyb.kindrep.generated.resources.routines_subtitle
import com.jhonnyb.kindrep.generated.resources.routines_title
import com.jhonnyb.kindrep.platform.KeepScreenAwakeEffect
import com.jhonnyb.kindrep.platform.PreferLandscapeOrientationEffect
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellIntent
import com.jhonnyb.kindrep.presentation.shell.ApplicationShellState
import com.jhonnyb.kindrep.ui.components.MainNavigationScaffold
import com.jhonnyb.kindrep.ui.screens.checkin.PostSessionCheckInScreen
import com.jhonnyb.kindrep.ui.screens.placeholder.UpcomingDetailScreen
import com.jhonnyb.kindrep.ui.screens.placeholder.UpcomingTabScreen
import com.jhonnyb.kindrep.ui.screens.player.GuidedSessionPlayerScreen
import com.jhonnyb.kindrep.ui.screens.today.TodayHomeScreen
import com.jhonnyb.kindrep.ui.screens.welcome.WelcomeDisclaimerScreen
import org.jetbrains.compose.resources.stringResource

private const val PREVIEW_SESSION_IDENTIFIER = "preview-session"

@Composable
fun KindrepNavigationHost(
    navigationController: NavHostController,
    shellState: ApplicationShellState,
    onShellIntent: (ApplicationShellIntent) -> Unit,
) {
    NavHost(
        navController = navigationController,
        startDestination = if (shellState.hasAcceptedDisclaimer) {
            KindrepDestination.Today
        } else {
            KindrepDestination.Welcome
        },
    ) {
        composable<KindrepDestination.Welcome> {
            WelcomeDisclaimerScreen(
                onAccept = { onShellIntent(ApplicationShellIntent.AcceptDisclaimer) },
            )
        }

        composable<KindrepDestination.Today> {
            MainNavigationScaffold(
                selectedDestination = TopLevelDestination.TODAY,
                onSelectDestination = { destination ->
                    onShellIntent(ApplicationShellIntent.SelectTopLevelDestination(destination))
                },
            ) {
                TodayHomeScreen(
                    streakDayCount = shellState.streakDayCount,
                    routine = shellState.activeRoutine,
                    onOpenSafetyNotice = {
                        onShellIntent(ApplicationShellIntent.OpenSafetyDialog)
                    },
                    onStartSession = { routineIdentifier ->
                        onShellIntent(ApplicationShellIntent.StartSession(routineIdentifier))
                    },
                )
            }
        }

        composable<KindrepDestination.Routines> {
            MainNavigationScaffold(
                selectedDestination = TopLevelDestination.ROUTINES,
                onSelectDestination = { destination ->
                    onShellIntent(ApplicationShellIntent.SelectTopLevelDestination(destination))
                },
            ) {
                UpcomingTabScreen(
                    title = stringResource(Res.string.routines_title),
                    subtitle = stringResource(Res.string.routines_subtitle),
                    primaryActionLabel = stringResource(Res.string.routines_create_action),
                    onPrimaryAction = {
                        navigationController.navigate(
                            KindrepDestination.RoutineEditor(routineIdentifier = null),
                        )
                    },
                )
            }
        }

        composable<KindrepDestination.Progress> {
            MainNavigationScaffold(
                selectedDestination = TopLevelDestination.PROGRESS,
                onSelectDestination = { destination ->
                    onShellIntent(ApplicationShellIntent.SelectTopLevelDestination(destination))
                },
            ) {
                UpcomingTabScreen(
                    title = stringResource(Res.string.progress_title),
                    subtitle = stringResource(Res.string.progress_subtitle),
                )
            }
        }

        composable<KindrepDestination.RoutineEditor> { backStackEntry ->
            val destination = backStackEntry.toRoute<KindrepDestination.RoutineEditor>()
            UpcomingDetailScreen(
                title = stringResource(
                    if (destination.routineIdentifier == null) {
                        Res.string.routine_editor_new_title
                    } else {
                        Res.string.routine_editor_edit_title
                    },
                ),
                subtitle = stringResource(Res.string.routine_editor_subtitle),
                onBack = { navigationController.popBackStack() },
            )
        }

        composable<KindrepDestination.ExerciseLibraryPicker> {
            UpcomingDetailScreen(
                title = stringResource(Res.string.exercise_picker_title),
                subtitle = stringResource(Res.string.exercise_picker_subtitle),
                onBack = { navigationController.popBackStack() },
            )
        }

        composable<KindrepDestination.GuidedSessionPlayer> { backStackEntry ->
            val destination = backStackEntry.toRoute<KindrepDestination.GuidedSessionPlayer>()
            PreferLandscapeOrientationEffect()
            KeepScreenAwakeEffect(enabled = true)
            GuidedSessionPlayerScreen(
                routineIdentifier = destination.routineIdentifier,
                onFinishToCheckIn = {
                    navigationController.navigate(
                        KindrepDestination.PostSessionCheckIn(
                            sessionIdentifier = PREVIEW_SESSION_IDENTIFIER,
                            routineName = shellState.activeRoutine.name,
                        ),
                    ) {
                        popUpTo(KindrepDestination.Today)
                    }
                },
                onExit = { navigationController.popBackStack() },
            )
        }

        composable<KindrepDestination.PostSessionCheckIn> { backStackEntry ->
            val destination = backStackEntry.toRoute<KindrepDestination.PostSessionCheckIn>()
            PostSessionCheckInScreen(
                routineName = destination.routineName,
                onDone = {
                    navigationController.navigate(KindrepDestination.Today) {
                        popUpTo(KindrepDestination.Today) { inclusive = true }
                    }
                },
            )
        }
    }
}
