package com.jhonnyb.kindrep.ui.screens.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.routines_create_action
import com.jhonnyb.kindrep.generated.resources.routines_subtitle
import com.jhonnyb.kindrep.generated.resources.routines_title
import com.jhonnyb.kindrep.navigation.TopLevelDestination
import com.jhonnyb.kindrep.ui.components.MainNavigationScaffold
import com.jhonnyb.kindrep.ui.components.ResponsiveScrollableColumn
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpcomingTabScreen(
    title: String,
    subtitle: String,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val palette = LocalKindrepPalette.current

    ResponsiveScrollableColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = palette.textPrimary,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = palette.textMuted,
        )
        if (primaryActionLabel != null && onPrimaryAction != null) {
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onPrimaryAction,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.primary,
                    contentColor = palette.onPrimary,
                ),
                modifier = Modifier.height(52.dp),
            ) {
                Text(
                    text = primaryActionLabel,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview(widthDp = 402, heightDp = 874)
@Composable
private fun UpcomingTabScreenPreview() {
    KindrepTheme {
        MainNavigationScaffold(
            selectedDestination = TopLevelDestination.ROUTINES,
            onSelectDestination = {},
        ) {
            UpcomingTabScreen(
                title = stringResource(Res.string.routines_title),
                subtitle = stringResource(Res.string.routines_subtitle),
                primaryActionLabel = stringResource(Res.string.routines_create_action),
                onPrimaryAction = {},
            )
        }
    }
}
