package com.cpower.billiardstrainingtracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cpower.billiardstrainingtracker.data.preferences.UserPreferencesRepository
import com.cpower.billiardstrainingtracker.data.repository.PracticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val practiceRepository: PracticeRepository,
    appVersion: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(appVersion = appVersion))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.isDarkMode.collect { isDarkMode ->
                _uiState.update { it.copy(isDarkMode = isDarkMode) }
            }
        }
    }

    fun onDarkModeChange(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkMode(enabled)
        }
    }

    fun showClearDataDialog() {
        _uiState.update { it.copy(showClearDataDialog = true) }
    }

    fun dismissClearDataDialog() {
        _uiState.update { it.copy(showClearDataDialog = false) }
    }

    fun clearAllRecords() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClearingData = true, showClearDataDialog = false) }
            practiceRepository.deleteAllRecords()
            _uiState.update { it.copy(isClearingData = false) }
        }
    }
}

class SettingsViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val practiceRepository: PracticeRepository,
    private val appVersion: String,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                userPreferencesRepository = userPreferencesRepository,
                practiceRepository = practiceRepository,
                appVersion = appVersion,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
