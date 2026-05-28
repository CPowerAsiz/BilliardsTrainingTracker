package com.cpower.billiardstrainingtracker

import android.content.Context
import androidx.room.Room
import com.cpower.billiardstrainingtracker.data.local.AppDatabase
import com.cpower.billiardstrainingtracker.data.preferences.UserPreferencesRepository
import com.cpower.billiardstrainingtracker.data.repository.PracticeRepository

class AppContainer(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "billiards_training_tracker.db",
    ).build()

    val practiceRepository: PracticeRepository = PracticeRepository(
        practiceRecordDao = database.practiceRecordDao(),
    )

    val userPreferencesRepository: UserPreferencesRepository = UserPreferencesRepository(
        context = context.applicationContext,
    )
}
