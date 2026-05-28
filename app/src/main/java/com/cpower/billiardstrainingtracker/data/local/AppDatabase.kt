package com.cpower.billiardstrainingtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PracticeRecordEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun practiceRecordDao(): PracticeRecordDao
}
