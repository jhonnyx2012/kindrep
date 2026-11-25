package com.jhonnyb.kindrep.navigation

import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.navigation_progress
import com.jhonnyb.kindrep.generated.resources.navigation_routines
import com.jhonnyb.kindrep.generated.resources.navigation_today
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestination(val labelResource: StringResource) {
    TODAY(Res.string.navigation_today),
    ROUTINES(Res.string.navigation_routines),
    PROGRESS(Res.string.navigation_progress),
}
