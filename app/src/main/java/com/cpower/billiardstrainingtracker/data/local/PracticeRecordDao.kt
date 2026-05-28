package com.cpower.billiardstrainingtracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeRecordDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: PracticeRecordEntity): Long

    @Query("SELECT * FROM practice_records ORDER BY practicedAt DESC")
    fun observeAll(): Flow<List<PracticeRecordEntity>>

    @Query("SELECT * FROM practice_records WHERE id = :id")
    fun observeById(id: Long): Flow<PracticeRecordEntity?>

    @Query("DELETE FROM practice_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM practice_records")
    suspend fun deleteAll()
}
