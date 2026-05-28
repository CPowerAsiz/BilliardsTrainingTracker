package com.cpower.billiardstrainingtracker.ui.records

data class RecordsUiState(
    val groupedRecords: List<RecordDateGroup> = emptyList(),
)

data class RecordDateGroup(
    val dateLabel: String,
    val records: List<RecordListItem>,
)

data class RecordListItem(
    val id: Long,
    val practiceTypeLabel: String,
    val totalBalls: Int,
    val successBalls: Int,
    val successRate: Int,
    val difficultyLabel: String,
    val practicedAtMillis: Long,
)
