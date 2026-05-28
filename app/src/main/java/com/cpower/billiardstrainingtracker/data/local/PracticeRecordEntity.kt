package com.cpower.billiardstrainingtracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "practice_records")
data class PracticeRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val practiceType: String,
    val totalBalls: Int,
    val successBalls: Int,
    val difficulty: String,
    val notes: String,
    val practicedAt: Long,
)
