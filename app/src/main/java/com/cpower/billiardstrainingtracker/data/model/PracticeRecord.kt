package com.cpower.billiardstrainingtracker.data.model

data class PracticeRecord(
    val id: Long,
    val practiceType: PracticeType,
    val totalBalls: Int,
    val successBalls: Int,
    val difficulty: Difficulty,
    val notes: String,
    val practicedAt: Long,
) {
    val successRate: Int
        get() = if (totalBalls > 0) (successBalls * 100) / totalBalls else 0
}
