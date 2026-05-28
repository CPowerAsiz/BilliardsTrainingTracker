package com.cpower.billiardstrainingtracker.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cpower.billiardstrainingtracker.data.repository.PracticeRepository
import com.cpower.billiardstrainingtracker.util.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordsViewModel(
    repository: PracticeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAllRecords().collect { records ->
                val listItems = records.map { record ->
                    RecordListItem(
                        id = record.id,
                        practiceTypeLabel = record.practiceType.displayName,
                        totalBalls = record.totalBalls,
                        successBalls = record.successBalls,
                        successRate = record.successRate,
                        difficultyLabel = record.difficulty.label,
                        practicedAtMillis = record.practicedAt,
                    )
                }
                val groupedRecords = listItems
                    .groupBy { DateTimeFormatter.formatDate(it.practicedAtMillis) }
                    .entries
                    .sortedByDescending { entry ->
                        entry.value.maxOf { it.practicedAtMillis }
                    }
                    .map { entry ->
                        RecordDateGroup(
                            dateLabel = entry.key,
                            records = entry.value,
                        )
                    }
                _uiState.update { it.copy(groupedRecords = groupedRecords) }
            }
        }
    }
}

class RecordsViewModelFactory(
    private val repository: PracticeRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
            return RecordsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
