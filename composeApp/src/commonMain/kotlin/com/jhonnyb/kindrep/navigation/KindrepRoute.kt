package com.jhonnyb.kindrep.navigation

import kotlinx.serialization.Serializable

enum class TopLevelTab(val label: String) {
    TODAY("Today"),
    ROUTINES("Routines"),
    PROGRESS("Progress"),
}

@Serializable
sealed interface KindrepRoute {
    @Serializable
    data object Welcome : KindrepRoute

    @Serializable
    data object Today : KindrepRoute

    @Serializable
    data object Routines : KindrepRoute

    @Serializable
    data class EditRoutine(val routineId: String? = null) : KindrepRoute

    @Serializable
    data class ExercisePicker(val routineId: String) : KindrepRoute

    @Serializable
    data class SessionPlayer(val routineId: String) : KindrepRoute

    @Serializable
    data class PostSessionCheckIn(
        val sessionId: String,
        val routineName: String,
    ) : KindrepRoute

    @Serializable
    data object Progress : KindrepRoute
}
