package com.cpower.billiardstrainingtracker.ui.records.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cpower.billiardstrainingtracker.data.repository.PracticeRepository
import com.cpower.billiardstrainingtracker.util.DateTimeFormatter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordDetailViewModel(
    private val repository: PracticeRepository,
    private val recordId: Long,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    private val _recordDeleted = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val recordDeleted: SharedFlow<Unit> = _recordDeleted.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.observeRecordById(recordId).collect { record ->
                _uiState.update {
                    it.copy(
                        record = record?.let { item ->
                            RecordDetailItem(
                                id = item.id,
                                practiceTypeLabel = item.practiceType.displayName,
                                successRate = item.successRate,
                                totalBalls = item.totalBalls,
                                successBalls = item.successBalls,
                                failedBalls = item.totalBalls - item.successBalls,
                                difficultyLabel = item.difficulty.label,
                                dateLabel = DateTimeFormatter.formatDate(item.practicedAt),
                                notes = item.notes,
                            )
                        },
                    )
                }
            }
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteRecord() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, showDeleteDialog = false) }
            repository.deleteRecord(recordId)
            _recordDeleted.emit(Unit)
        }
    }
}

class RecordDetailViewModelFactory(
    private val repository: PracticeRepository,
    private val recordId: Long,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordDetailViewModel::class.java)) {
            return RecordDetailViewModel(repository, recordId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
