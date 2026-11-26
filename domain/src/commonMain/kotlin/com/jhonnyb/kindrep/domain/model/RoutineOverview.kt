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
            identifier = "prescribed-recovery-program",
            name = "Back, Hips & Knee Program",
            estimatedMinutes = 58,
            breakDurationSeconds = 10,
            completedSessionsToday = 1,
            dailyTargetSessions = 2,
            exercises = listOf(
                ExerciseSummary(orderNumber = 1, title = "Cat-Cow", targetDescription = "5 reps · 2 sets · 5s hold"),
                ExerciseSummary(orderNumber = 2, title = "Child's Pose", targetDescription = "1 rep · 2 sets · 30s hold"),
                ExerciseSummary(orderNumber = 3, title = "McGill Curl-Up", targetDescription = "10 reps · 3 sets · 5s hold"),
                ExerciseSummary(orderNumber = 4, title = "Glute Bridge", targetDescription = "10 reps · 3 sets · 5s hold"),
                ExerciseSummary(orderNumber = 5, title = "Hamstring Bridge", targetDescription = "6 reps · 3 sets · 5s hold"),
                ExerciseSummary(orderNumber = 6, title = "Lying Leg Curl", targetDescription = "12 reps · 3 sets · 2s hold"),
                ExerciseSummary(orderNumber = 7, title = "Single-Leg Hip Hinge", targetDescription = "12 reps · 3 sets · each side"),
                ExerciseSummary(orderNumber = 8, title = "Bent-Knee Heel Raise", targetDescription = "15 reps · 3 sets · each side"),
                ExerciseSummary(orderNumber = 9, title = "Standing Calf Raise", targetDescription = "15 reps · 3 sets"),
                ExerciseSummary(orderNumber = 10, title = "Wall Calf Stretch", targetDescription = "1 rep · 3 sets · 30s hold"),
                ExerciseSummary(orderNumber = 11, title = "Hip Hinge (Deadlift)", targetDescription = "12 reps · 3 sets"),
                ExerciseSummary(orderNumber = 12, title = "Piriformis Stretch", targetDescription = "1 rep · 3 sets · 30s hold"),
                ExerciseSummary(orderNumber = 13, title = "Seated Glute Release", targetDescription = "1 rep · 3 sets · 30s hold"),
            ),
        )
    }
}
