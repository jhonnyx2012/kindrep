package com.jhonnyb.kindrep

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.jhonnyb.kindrep.navigation.KindrepNavigationHost
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

        if (shellState.isSafetyDialogVisible) {
            SafetyNoticeDialog(
                onDismiss = { shellViewModel.setSafetyDialogVisible(false) },
            )
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = palette.canvas,
        ) {
            KindrepNavigationHost(
                navigationController = navigationController,
                shellState = shellState,
                shellViewModel = shellViewModel,
            )
        }
    }
}
