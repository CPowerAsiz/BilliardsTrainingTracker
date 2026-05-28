package com.cpower.billiardstrainingtracker.ui.records.detail

data class RecordDetailUiState(
    val record: RecordDetailItem? = null,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false,
)

data class RecordDetailItem(
    val id: Long,
    val practiceTypeLabel: String,
    val successRate: Int,
    val totalBalls: Int,
    val successBalls: Int,
    val failedBalls: Int,
    val difficultyLabel: String,
    val dateLabel: String,
    val notes: String,
)
