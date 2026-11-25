package com.jhonnyb.kindrep.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface KindrepDestination {
    @Serializable
    data object Welcome : KindrepDestination

    @Serializable
    data object Today : KindrepDestination

    @Serializable
    data object Routines : KindrepDestination

    @Serializable
    data class RoutineEditor(val routineIdentifier: String? = null) : KindrepDestination

    @Serializable
    data class ExerciseLibraryPicker(val routineIdentifier: String) : KindrepDestination

    @Serializable
    data class GuidedSessionPlayer(val routineIdentifier: String) : KindrepDestination

    @Serializable
    data class PostSessionCheckIn(
        val sessionIdentifier: String,
        val routineName: String,
    ) : KindrepDestination

    @Serializable
    data object Progress : KindrepDestination
}
