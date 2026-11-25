package com.jhonnyb.kindrep.domain.model

data class RoutineOverview(
    val identifier: String,
    val name: String,
    val estimatedMinutes: Int,
    val breakDurationSeconds: Int,
    val completedSessionsToday: Int,
    val dailyTargetSessions: Int,
    val exercises: List<ExerciseSummary>,
) {
    val visibleExercises: List<ExerciseSummary>
        get() = exercises.take(MAXIMUM_VISIBLE_EXERCISES)

    val hiddenExerciseCount: Int
        get() = (exercises.size - MAXIMUM_VISIBLE_EXERCISES).coerceAtLeast(0)

    val dailyProgressBadge: String
        get() = "$completedSessionsToday / $dailyTargetSessions"

    companion object {
        const val MAXIMUM_VISIBLE_EXERCISES: Int = 4

        fun sampleLowerBackRecovery(): RoutineOverview = RoutineOverview(
            identifier = "lower-back-recovery",
            name = "Lower Back Recovery",
            estimatedMinutes = 8,
            breakDurationSeconds = 5,
            completedSessionsToday = 1,
            dailyTargetSessions = 2,
            exercises = listOf(
                ExerciseSummary(orderNumber = 1, title = "Cat-Cow", targetDescription = "10 reps"),
                ExerciseSummary(orderNumber = 2, title = "Bird Dog", targetDescription = "8 / side"),
                ExerciseSummary(orderNumber = 3, title = "Glute Bridge", targetDescription = "12 reps · 2 sets"),
                ExerciseSummary(orderNumber = 4, title = "Dead Bug", targetDescription = "8 / side"),
                ExerciseSummary(orderNumber = 5, title = "Child's Pose", targetDescription = "45s hold"),
            ),
        )
    }
}
