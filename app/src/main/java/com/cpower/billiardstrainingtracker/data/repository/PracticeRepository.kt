package com.cpower.billiardstrainingtracker.data.repository

import com.cpower.billiardstrainingtracker.data.local.PracticeRecordDao
import com.cpower.billiardstrainingtracker.data.local.PracticeRecordEntity
import com.cpower.billiardstrainingtracker.data.model.Difficulty
import com.cpower.billiardstrainingtracker.data.model.PracticeRecord
import com.cpower.billiardstrainingtracker.data.model.PracticeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PracticeRepository(
    private val practiceRecordDao: PracticeRecordDao,
) {

    fun observeAllRecords(): Flow<List<PracticeRecord>> =
        practiceRecordDao.observeAll().map { entities ->
            entities.map { it.toPracticeRecord() }
        }

    fun observeRecordById(id: Long): Flow<PracticeRecord?> =
        practiceRecordDao.observeById(id).map { entity ->
            entity?.toPracticeRecord()
        }

    suspend fun deleteRecord(id: Long) {
        practiceRecordDao.deleteById(id)
    }

    suspend fun deleteAllRecords() {
        practiceRecordDao.deleteAll()
    }

    suspend fun insertRecord(
        practiceType: PracticeType,
        totalBalls: Int,
        successBalls: Int,
        difficulty: Difficulty,
        notes: String,
        practicedAt: Long,
    ): Long {
        return practiceRecordDao.insert(
            PracticeRecordEntity(
                practiceType = practiceType.name,
                totalBalls = totalBalls,
                successBalls = successBalls,
                difficulty = difficulty.name,
                notes = notes.trim(),
                practicedAt = practicedAt,
            ),
        )
    }

    private fun PracticeRecordEntity.toPracticeRecord(): PracticeRecord =
        PracticeRecord(
            id = id,
            practiceType = PracticeType.fromStoredValue(practiceType),
            totalBalls = totalBalls,
            successBalls = successBalls,
            difficulty = Difficulty.fromStoredValue(difficulty),
            notes = notes,
            practicedAt = practicedAt,
        )
}
