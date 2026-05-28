package com.cpower.billiardstrainingtracker.ui.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cpower.billiardstrainingtracker.data.model.Difficulty
import com.cpower.billiardstrainingtracker.data.model.PracticeType
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

class PracticeViewModel(
    private val repository: PracticeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    private val _recordSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val recordSaved: SharedFlow<Unit> = _recordSaved.asSharedFlow()

    fun onPracticeTypeChange(type: PracticeType) {
        _uiState.update {
            it.copy(
                practiceType = type,
                practiceTypeMenuExpanded = false,
            )
        }
    }

    fun onPracticeTypeMenuExpandedChange(expanded: Boolean) {
        _uiState.update { it.copy(practiceTypeMenuExpanded = expanded) }
    }

    fun onTotalBallsChange(value: String) {
        _uiState.update {
            it.copy(
                totalBalls = value.filter { char -> char.isDigit() },
                totalBallsError = null,
                successBallsError = null,
            )
        }
    }

    fun onSuccessBallsChange(value: String) {
        _uiState.update {
            it.copy(
                successBalls = value.filter { char -> char.isDigit() },
                successBallsError = null,
            )
        }
    }

    fun onDifficultyChange(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun onNotesChange(value: String) {
        _uiState.update { it.copy(notes = value) }
    }

    fun saveRecord() {
        val state = _uiState.value
        val totalBalls = state.totalBalls.toIntOrNull()
        val successBalls = state.successBalls.toIntOrNull()

        var totalBallsError: PracticeFieldError? = null
        var successBallsError: PracticeFieldError? = null

        if (totalBalls == null || totalBalls <= 0) {
            totalBallsError = PracticeFieldError.INVALID_TOTAL
        }
        if (successBalls == null) {
            successBallsError = PracticeFieldError.INVALID_SUCCESS
        } else if (totalBalls != null && successBalls > totalBalls) {
            successBallsError = PracticeFieldError.SUCCESS_EXCEEDS_TOTAL
        }

        if (totalBallsError != null || successBallsError != null) {
            _uiState.update {
                it.copy(
                    totalBallsError = totalBallsError,
                    successBallsError = successBallsError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            repository.insertRecord(
                practiceType = state.practiceType,
                totalBalls = totalBalls!!,
                successBalls = successBalls!!,
                difficulty = state.difficulty,
                notes = state.notes,
                practicedAt = state.practicedAtMillis,
            )
            _uiState.value = createInitialState()
            _recordSaved.emit(Unit)
        }
    }

    private fun createInitialState(): PracticeUiState {
        val practicedAt = System.currentTimeMillis()
        return PracticeUiState(
            practicedAtMillis = practicedAt,
            practicedAtFormatted = DateTimeFormatter.formatDateTime(practicedAt),
        )
    }
}

class PracticeViewModelFactory(
    private val repository: PracticeRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            return PracticeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
