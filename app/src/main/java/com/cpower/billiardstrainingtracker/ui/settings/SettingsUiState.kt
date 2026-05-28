package com.cpower.billiardstrainingtracker.ui.settings

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val showClearDataDialog: Boolean = false,
    val isClearingData: Boolean = false,
    val appVersion: String = "1.0",
)
