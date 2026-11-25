package com.jhonnyb.kindrep.domain.model

data class ExerciseSummary(
    val orderNumber: Int,
    val title: String,
    val targetDescription: String,
) {
    val numberedTitle: String
        get() = "$orderNumber. $title"
}
