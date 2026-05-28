package com.cpower.billiardstrainingtracker.ui.home

data class HomeUiState(
    val hasPracticedToday: Boolean = false,
    val weeklyPracticeCount: Int = 0,
    val weeklyAverageSuccessRate: Int = 0,
    val lastPractice: LastPractice? = null,
    val weakestItem: WeakestItem? = null,
)

data class LastPractice(
    val drillName: String,
    val totalBalls: Int,
    val successBalls: Int,
) {
    val successRate: Int
        get() = if (totalBalls > 0) (successBalls * 100) / totalBalls else 0
}

data class WeakestItem(
    val name: String,
    val successRate: Int,
)

fun previewHomeUiState(): HomeUiState = HomeUiState(
    hasPracticedToday = false,
    weeklyPracticeCount = 3,
    weeklyAverageSuccessRate = 62,
    lastPractice = LastPractice(
        drillName = "拉桿練習",
        totalBalls = 20,
        successBalls = 12,
    ),
    weakestItem = WeakestItem(
        name = "高低桿控制",
        successRate = 45,
    ),
)
