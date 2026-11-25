package com.jhonnyb.kindrep.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette

@Composable
fun MainNavigationScaffold(
    selectedDestination: TopLevelDestination,
    onSelectDestination: (TopLevelDestination) -> Unit,
    content: @Composable () -> Unit,
) {
    val palette = LocalKindrepPalette.current

    Scaffold(
        containerColor = palette.canvas,
        bottomBar = {
            KindrepBottomNavigationBar(
                selectedDestination = selectedDestination,
                onSelectDestination = onSelectDestination,
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

@Preview
@Composable
private fun MainNavigationScaffoldPreview() {
    KindrepTheme {
        MainNavigationScaffold(
            selectedDestination = TopLevelDestination.TODAY,
            onSelectDestination = {},
            content = {},
        )
    }
}
