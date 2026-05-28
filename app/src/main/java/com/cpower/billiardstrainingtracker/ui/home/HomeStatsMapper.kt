package com.cpower.billiardstrainingtracker.ui.home

import com.cpower.billiardstrainingtracker.data.model.PracticeRecord
import java.util.Calendar

fun List<PracticeRecord>.toHomeUiState(
    nowMillis: Long = System.currentTimeMillis(),
): HomeUiState {
    if (isEmpty()) {
        return HomeUiState()
    }

    val hasPracticedToday = any { isSameDay(it.practicedAt, nowMillis) }
    val weekRecords = filter { isSameWeek(it.practicedAt, nowMillis) }
    val weeklyPracticeCount = weekRecords.size
    val weeklyAverageSuccessRate = weekRecords.weightedSuccessRate()

    val latest = maxByOrNull { it.practicedAt }
    val lastPractice = latest?.let {
        LastPractice(
            drillName = it.practiceType.displayName,
            totalBalls = it.totalBalls,
            successBalls = it.successBalls,
        )
    }

    val weakestItem = findWeakestPracticeType()

    return HomeUiState(
        hasPracticedToday = hasPracticedToday,
        weeklyPracticeCount = weeklyPracticeCount,
        weeklyAverageSuccessRate = weeklyAverageSuccessRate,
        lastPractice = lastPractice,
        weakestItem = weakestItem,
    )
}

private fun List<PracticeRecord>.findWeakestPracticeType(): WeakestItem? {
    val grouped = groupBy { it.practiceType }
    if (grouped.size < 2) return null

    val typeRates = grouped.map { (type, records) ->
        type to records.weightedSuccessRate()
    }
    val weakest = typeRates.minByOrNull { it.second } ?: return null
    return WeakestItem(
        name = weakest.first.displayName,
        successRate = weakest.second,
    )
}

private fun List<PracticeRecord>.weightedSuccessRate(): Int {
    val totalBalls = sumOf { it.totalBalls }
    val successBalls = sumOf { it.successBalls }
    return if (totalBalls > 0) (successBalls * 100) / totalBalls else 0
}

private fun isSameDay(recordMillis: Long, nowMillis: Long): Boolean {
    val recordCal = Calendar.getInstance().apply { timeInMillis = recordMillis }
    val nowCal = Calendar.getInstance().apply { timeInMillis = nowMillis }
    return recordCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
        recordCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)
}

private fun isSameWeek(recordMillis: Long, nowMillis: Long): Boolean {
    val recordCal = Calendar.getInstance().apply { timeInMillis = recordMillis }
    val nowCal = Calendar.getInstance().apply { timeInMillis = nowMillis }
    return recordCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
        recordCal.get(Calendar.WEEK_OF_YEAR) == nowCal.get(Calendar.WEEK_OF_YEAR)
}
