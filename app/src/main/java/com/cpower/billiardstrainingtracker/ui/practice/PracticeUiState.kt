package com.cpower.billiardstrainingtracker.ui.practice

import com.cpower.billiardstrainingtracker.data.model.Difficulty
import com.cpower.billiardstrainingtracker.data.model.PracticeType

data class PracticeUiState(
    val practiceType: PracticeType = PracticeType.DRAW,
    val practiceTypeMenuExpanded: Boolean = false,
    val totalBalls: String = "20",
    val successBalls: String = "12",
    val difficulty: Difficulty = Difficulty.EASY,
    val notes: String = "",
    val practicedAtMillis: Long = System.currentTimeMillis(),
    val practicedAtFormatted: String = "",
    val totalBallsError: PracticeFieldError? = null,
    val successBallsError: PracticeFieldError? = null,
    val isSaving: Boolean = false,
)

enum class PracticeFieldError {
    INVALID_TOTAL,
    INVALID_SUCCESS,
    SUCCESS_EXCEEDS_TOTAL,
}
