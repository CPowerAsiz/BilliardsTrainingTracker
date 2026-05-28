package com.cpower.billiardstrainingtracker.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cpower.billiardstrainingtracker.BuildConfig
import com.cpower.billiardstrainingtracker.R
import com.cpower.billiardstrainingtracker.ui.rememberAppContainer
import com.cpower.billiardstrainingtracker.ui.theme.BilliardsTrainingTrackerTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {
    val container = rememberAppContainer()
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            userPreferencesRepository = container.userPreferencesRepository,
            practiceRepository = container.practiceRepository,
            appVersion = BuildConfig.VERSION_NAME,
        ),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        uiState = uiState,
        onDarkModeChange = viewModel::onDarkModeChange,
        onClearDataClick = viewModel::showClearDataDialog,
        onDismissClearDataDialog = viewModel::dismissClearDataDialog,
        onConfirmClearData = viewModel::clearAllRecords,
        modifier = modifier,
    )
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onDarkModeChange: (Boolean) -> Unit,
    onClearDataClick: () -> Unit,
    onDismissClearDataDialog: () -> Unit,
    onConfirmClearData: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.showClearDataDialog) {
        AlertDialog(
            onDismissRequest = onDismissClearDataDialog,
            title = { Text(stringResource(R.string.settings_clear_data_dialog_title)) },
            text = { Text(stringResource(R.string.settings_clear_data_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = onConfirmClearData,
                    enabled = !uiState.isClearingData,
                ) {
                    Text(stringResource(R.string.settings_clear_data_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissClearDataDialog) {
                    Text(stringResource(R.string.settings_clear_data_cancel))
                }
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        SettingsSectionCard(title = stringResource(R.string.settings_appearance_title)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.settings_dark_mode),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = uiState.isDarkMode,
                    onCheckedChange = onDarkModeChange,
                )
            }
        }

        SettingsSectionCard(title = stringResource(R.string.settings_data_title)) {
            OutlinedButton(
                onClick = onClearDataClick,
                enabled = !uiState.isClearingData,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.settings_clear_all_records))
            }
        }

        SettingsSectionCard(title = stringResource(R.string.settings_about_title)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.settings_version, uiState.appVersion),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            content()
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    BilliardsTrainingTrackerTheme {
        Surface {
            SettingsScreenContent(
                uiState = SettingsUiState(isDarkMode = false, appVersion = "1.0"),
                onDarkModeChange = {},
                onClearDataClick = {},
                onDismissClearDataDialog = {},
                onConfirmClearData = {},
            )
        }
    }
}
