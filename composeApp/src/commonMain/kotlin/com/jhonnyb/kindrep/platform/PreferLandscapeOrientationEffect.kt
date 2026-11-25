package com.jhonnyb.kindrep.platform

import androidx.compose.runtime.Composable

/**
 * Requests sensor-landscape orientation ONLY while the Guided Session Player is active,
 * restoring the user's natural (unspecified) device orientation on exit so phones and
 * tablets rotate freely across all other screens.
 */
@Composable
expect fun PreferLandscapeOrientationEffect()
