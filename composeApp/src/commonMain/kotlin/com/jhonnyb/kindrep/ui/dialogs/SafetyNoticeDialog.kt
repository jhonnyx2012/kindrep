package com.jhonnyb.kindrep.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.safety_dialog_confirm
import com.jhonnyb.kindrep.generated.resources.safety_dialog_point_follow_plan
import com.jhonnyb.kindrep.generated.resources.safety_dialog_point_no_prescription
import com.jhonnyb.kindrep.generated.resources.safety_dialog_point_stop_on_pain
import com.jhonnyb.kindrep.generated.resources.safety_dialog_title
import com.jhonnyb.kindrep.ui.theme.KindrepTheme
import com.jhonnyb.kindrep.ui.theme.LocalKindrepPalette
import org.jetbrains.compose.resources.stringResource

@Composable
fun SafetyNoticeDialog(
    onDismiss: () -> Unit,
) {
    val palette = LocalKindrepPalette.current

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        containerColor = palette.surface,
        title = {
            Text(
                text = stringResource(Res.string.safety_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                color = palette.textPrimary,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.safety_dialog_point_no_prescription),
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
                Text(
                    text = stringResource(Res.string.safety_dialog_point_follow_plan),
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.textPrimary,
                )
                Text(
                    text = stringResource(Res.string.safety_dialog_point_stop_on_pain),
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
                Text(
                    text = stringResource(Res.string.safety_dialog_confirm),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
    )
}

@Preview
@Composable
private fun SafetyNoticeDialogPreview() {
    KindrepTheme {
        SafetyNoticeDialog(onDismiss = {})
    }
}
