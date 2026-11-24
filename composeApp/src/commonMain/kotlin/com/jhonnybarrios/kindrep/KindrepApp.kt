package com.jhonnybarrios.kindrep

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jhonnybarrios.kindrep.navigation.KindrepRoute
import com.jhonnybarrios.kindrep.navigation.TopLevelTab
import com.jhonnybarrios.kindrep.platform.KeepScreenAwake
import com.jhonnybarrios.kindrep.platform.LockScreenOrientation
import com.jhonnybarrios.kindrep.platform.LoopingExerciseVideo
import com.jhonnybarrios.kindrep.presentation.AppShellViewModel
import com.jhonnybarrios.kindrep.ui.components.KindrepBottomBar
import com.jhonnybarrios.kindrep.ui.components.KindrepBunnyMascot
import com.jhonnybarrios.kindrep.ui.components.MascotPose
import com.jhonnybarrios.kindrep.ui.theme.KindrepTheme
import com.jhonnybarrios.kindrep.ui.theme.LocalKindrepPalette
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun KindrepApp() {
    KindrepTheme {
        val viewModel: AppShellViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            val palette = LocalKindrepPalette.current

            if (uiState.isSafetyModalVisible) {
                SafetyNoticeDialog(onDismiss = { viewModel.setSafetyModalVisible(false) })
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = palette.canvas,
            ) {
                NavHost(
                    navController = navController,
                    startDestination = if (uiState.hasAcceptedDisclaimer) {
                        KindrepRoute.Today
                    } else {
                        KindrepRoute.Welcome
                    },
                ) {
                    composable<KindrepRoute.Welcome> {
                        LockScreenOrientation(landscape = false)
                        WelcomeDisclaimerScreen(
                            onAccept = {
                                viewModel.acceptDisclaimer()
                                navController.navigate(KindrepRoute.Today) {
                                    popUpTo(KindrepRoute.Welcome) { inclusive = true }
                                }
                            },
                        )
                    }

                    composable<KindrepRoute.Today> {
                        LockScreenOrientation(landscape = false)
                        MainTabScaffold(
                            selectedTab = TopLevelTab.TODAY,
                            onSelectTab = { tab ->
                                viewModel.selectTab(tab)
                                when (tab) {
                                    TopLevelTab.TODAY -> Unit
                                    TopLevelTab.ROUTINES -> navController.navigate(KindrepRoute.Routines) { launchSingleTop = true }
                                    TopLevelTab.PROGRESS -> navController.navigate(KindrepRoute.Progress) { launchSingleTop = true }
                                }
                            },
                        ) {
                            TodayHomeScreen(
                                onOpenSafetyNotice = { viewModel.setSafetyModalVisible(true) },
                                onStartSession = { routineId ->
                                    viewModel.previewAudioCue("ready")
                                    navController.navigate(KindrepRoute.SessionPlayer(routineId = routineId))
                                },
                            )
                        }
                    }

                    composable<KindrepRoute.Routines> {
                        LockScreenOrientation(landscape = false)
                        MainTabScaffold(
                            selectedTab = TopLevelTab.ROUTINES,
                            onSelectTab = { tab ->
                                viewModel.selectTab(tab)
                                when (tab) {
                                    TopLevelTab.TODAY -> navController.navigate(KindrepRoute.Today) { launchSingleTop = true }
                                    TopLevelTab.ROUTINES -> Unit
                                    TopLevelTab.PROGRESS -> navController.navigate(KindrepRoute.Progress) { launchSingleTop = true }
                                }
                            },
                        ) {
                            PlaceholderTabScreen(
                                title = "Routines",
                                subtitle = "Manage all practitioner-prescribed plans (Week 2).",
                                primaryActionLabel = "+ Create Routine",
                                onPrimaryAction = {
                                    navController.navigate(KindrepRoute.EditRoutine(routineId = null))
                                },
                            )
                        }
                    }

                    composable<KindrepRoute.Progress> {
                        LockScreenOrientation(landscape = false)
                        MainTabScaffold(
                            selectedTab = TopLevelTab.PROGRESS,
                            onSelectTab = { tab ->
                                viewModel.selectTab(tab)
                                when (tab) {
                                    TopLevelTab.TODAY -> navController.navigate(KindrepRoute.Today) { launchSingleTop = true }
                                    TopLevelTab.ROUTINES -> navController.navigate(KindrepRoute.Routines) { launchSingleTop = true }
                                    TopLevelTab.PROGRESS -> Unit
                                }
                            },
                        ) {
                            PlaceholderTabScreen(
                                title = "Progress",
                                subtitle = "Consistency calendar & 14-day pain trend (Week 3).",
                            )
                        }
                    }

                    composable<KindrepRoute.EditRoutine> { backStackEntry ->
                        LockScreenOrientation(landscape = false)
                        val route = backStackEntry.toRoute<KindrepRoute.EditRoutine>()
                        PlaceholderSubScreen(
                            title = if (route.routineId == null) "New Routine" else "Edit Routine",
                            subtitle = "Routine Builder & Exercise Picker arriving in Week 2.",
                            onBack = { navController.popBackStack() },
                        )
                    }

                    composable<KindrepRoute.ExercisePicker> {
                        LockScreenOrientation(landscape = false)
                        PlaceholderSubScreen(
                            title = "Exercise Library",
                            subtitle = "Add only exercises recommended by your practitioner.",
                            onBack = { navController.popBackStack() },
                        )
                    }

                    composable<KindrepRoute.SessionPlayer> { backStackEntry ->
                        val route = backStackEntry.toRoute<KindrepRoute.SessionPlayer>()
                        LockScreenOrientation(landscape = true)
                        KeepScreenAwake(enabled = true)
                        GuidedPlayerScaffoldScreen(
                            routineId = route.routineId,
                            onFinishToCheckIn = {
                                navController.navigate(
                                    KindrepRoute.PostSessionCheckIn(
                                        sessionId = "preview-session",
                                        routineName = "Lower Back Recovery",
                                    ),
                                ) {
                                    popUpTo(KindrepRoute.Today)
                                }
                            },
                            onExit = { navController.popBackStack() },
                        )
                    }

                    composable<KindrepRoute.PostSessionCheckIn> { backStackEntry ->
                        LockScreenOrientation(landscape = false)
                        val route = backStackEntry.toRoute<KindrepRoute.PostSessionCheckIn>()
                        PostSessionCheckInScaffoldScreen(
                            routineName = route.routineName,
                            onDone = {
                                navController.navigate(KindrepRoute.Today) {
                                    popUpTo(KindrepRoute.Today) { inclusive = true }
                                }
                            },
                        )
                    }
                }
            }
        }
}

@Composable
private fun MainTabScaffold(
    selectedTab: TopLevelTab,
    onSelectTab: (TopLevelTab) -> Unit,
    content: @Composable () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    Scaffold(
        containerColor = palette.canvas,
        bottomBar = {
            KindrepBottomBar(
                selectedTab = selectedTab,
                onSelectTab = onSelectTab,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            content()
        }
    }
}

@Composable
private fun WelcomeDisclaimerScreen(
    onAccept: () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        KindrepBunnyMascot(
            pose = MascotPose.Welcome,
            modifier = Modifier.size(width = 240.dp, height = 180.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Welcome to Kindrep",
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Your calm companion for staying consistent with the recovery plan your practitioner prescribed.",
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = palette.surface,
            border = BorderStroke(1.dp, palette.border),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "BEFORE YOU START",
                    style = MaterialTheme.typography.labelMedium,
                    color = palette.secondary,
                )
                Text(
                    text = "• Kindrep doesn't prescribe exercises or replace your practitioner.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
                Text(
                    text = "• Add only the plan your physio, chiro, or clinician gave you.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
                Text(
                    text = "• Stop right away if you feel sharp or worsening pain.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onAccept,
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
                text = "I Understand · Build My Routine",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "All recovery data stays private on your device.",
            style = MaterialTheme.typography.bodyMedium,
            color = palette.textMuted,
        )
    }
}

@Composable
private fun TodayHomeScreen(
    onOpenSafetyNotice: () -> Unit,
    onStartSession: (String) -> Unit,
) {
    val palette = LocalKindrepPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Good morning",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textMuted,
                )
                Text(
                    text = "Kindrep",
                    style = MaterialTheme.typography.headlineLarge,
                    color = palette.textPrimary,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(palette.secondarySoft)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "5-day streak",
                        style = MaterialTheme.typography.labelMedium,
                        color = palette.secondary,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(palette.surface)
                        .border(1.dp, palette.border, CircleShape)
                        .clickable(onClick = onOpenSafetyNotice),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "i",
                        style = MaterialTheme.typography.labelMedium,
                        color = palette.textPrimary,
                    )
                }
            }
        }

        // Today Companion Streak Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = palette.surface,
            border = BorderStroke(1.dp, palette.border),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                KindrepBunnyMascot(
                    pose = MascotPose.TodayCompanion,
                    modifier = Modifier.size(width = 96.dp, height = 72.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "1 more session for today",
                        style = MaterialTheme.typography.titleMedium,
                        color = palette.textPrimary,
                    )
                    Text(
                        text = "Steady beats intense. Listen to your body.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textMuted,
                    )
                }
            }
        }

        // Equal Routine Card Preview (capped at 4 exercises + ellipsis, real numeric values only)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = palette.surface,
            border = BorderStroke(1.dp, palette.border),
            modifier = Modifier.fillMaxWidth(),
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
                    Column {
                        Text(
                            text = "Lower Back Recovery",
                            style = MaterialTheme.typography.titleLarge,
                            color = palette.textPrimary,
                        )
                        Text(
                            text = "5 exercises · ~8 min · 5s breaks",
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
                            text = "1 / 2",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.secondary,
                        )
                    }
                }

                val rows = listOf(
                    "1. Cat-Cow" to "10 reps",
                    "2. Bird Dog" to "8 / side",
                    "3. Glute Bridge" to "12 reps · 2 sets",
                    "4. Dead Bug" to "8 / side",
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    rows.forEach { (name, target) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(name, style = MaterialTheme.typography.bodyLarge, color = palette.textPrimary)
                            Text(target, style = MaterialTheme.typography.bodyMedium, color = palette.textMuted)
                        }
                    }
                    Text(
                        text = "+ 1 more exercise...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = palette.textMuted,
                    )
                }

                Button(
                    onClick = { onStartSession("lower-back-recovery") },
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
                        text = "▶  Start Session",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun GuidedPlayerScaffoldScreen(
    routineId: String,
    onFinishToCheckIn: () -> Unit,
    onExit: () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.canvas)
            .safeDrawingPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1.3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp)),
        ) {
            LoopingExerciseVideo(
                mediaKey = routineId,
                isPlaying = true,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Bird Dog · Left Side",
                    style = MaterialTheme.typography.titleLarge,
                    color = palette.textPrimary,
                )
                TextButton(onClick = onExit) {
                    Text("Exit", color = palette.textMuted)
                }
            }
            Column {
                Text(
                    text = "Rep 4 / 8",
                    style = MaterialTheme.typography.displayLarge,
                    color = palette.textPrimary,
                )
                Text(
                    text = "Hands-free auto-pacer & landscape HUD arriving in Week 2.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textMuted,
                )
            }
            Button(
                onClick = onFinishToCheckIn,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text("Preview Post-Session Check-In", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun PostSessionCheckInScaffoldScreen(
    routineName: String,
    onDone: () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        KindrepBunnyMascot(
            pose = MascotPose.Celebration,
            modifier = Modifier.size(width = 240.dp, height = 180.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Session Complete",
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "$routineName · Pain & feeling check-in arriving in Week 3.",
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onDone,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.primary,
                contentColor = palette.onPrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text("Back to Today", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun PlaceholderTabScreen(
    title: String,
    subtitle: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
) {
    val palette = LocalKindrepPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(title, style = MaterialTheme.typography.headlineLarge, color = palette.textPrimary)
        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = palette.textMuted)
        if (primaryActionLabel != null && onPrimaryAction != null) {
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onPrimaryAction,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier.height(52.dp),
            ) {
                Text(primaryActionLabel, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun PlaceholderSubScreen(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedButton(
            onClick = onBack,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("← Back", color = palette.textPrimary, fontWeight = FontWeight.SemiBold)
        }
        Text(title, style = MaterialTheme.typography.headlineLarge, color = palette.textPrimary)
        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = palette.textMuted)
    }
}

@Composable
private fun SafetyNoticeDialog(
    onDismiss: () -> Unit,
) {
    val palette = LocalKindrepPalette.current
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        containerColor = palette.surface,
        title = {
            Text(
                text = "Safety & About Kindrep",
                style = MaterialTheme.typography.titleLarge,
                color = palette.textPrimary,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "• Kindrep doesn't prescribe exercises or replace a licensed physio, chiro, osteo, or clinician.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
                Text(
                    text = "• Follow only the exercises, sets, and holds recommended for your recovery.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
                Text(
                    text = "• Stop immediately if you experience sharp, radiating, or worsening pain.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
            ) {
                Text("Got it", style = MaterialTheme.typography.labelMedium)
            }
        },
    )
}
