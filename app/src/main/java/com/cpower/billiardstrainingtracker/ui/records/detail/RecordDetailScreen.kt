package com.cpower.billiardstrainingtracker.ui.records.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cpower.billiardstrainingtracker.R
import com.cpower.billiardstrainingtracker.ui.rememberAppContainer
import com.cpower.billiardstrainingtracker.ui.theme.BilliardsTrainingTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    recordId: Long,
    onNavigateBack: () -> Unit,
    onRecordDeleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val container = rememberAppContainer()
    val viewModel: RecordDetailViewModel = viewModel(
        factory = RecordDetailViewModelFactory(
            repository = container.practiceRepository,
            recordId = recordId,
        ),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.recordDeleted.collect {
            onRecordDeleted()
        }
    }

    RecordDetailScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onDeleteClick = viewModel::showDeleteDialog,
        onDismissDeleteDialog = viewModel::dismissDeleteDialog,
        onConfirmDelete = viewModel::deleteRecord,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreenContent(
    uiState: RecordDetailUiState,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val record = uiState.record

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = { Text(stringResource(R.string.detail_delete_dialog_title)) },
            text = { Text(stringResource(R.string.detail_delete_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = onConfirmDelete,
                    enabled = !uiState.isDeleting,
                ) {
                    Text(stringResource(R.string.detail_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteDialog) {
                    Text(stringResource(R.string.detail_delete_cancel))
                }
            },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (record == null) {
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = record.practiceTypeLabel,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.detail_success_rate_label),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(R.string.detail_success_rate_value, record.successRate),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            DetailInfoCard(
                label = stringResource(R.string.detail_total_balls),
                value = record.totalBalls.toString(),
            )
            DetailInfoCard(
                label = stringResource(R.string.detail_success_balls),
                value = record.successBalls.toString(),
            )
            DetailInfoCard(
                label = stringResource(R.string.detail_failed_balls),
                value = record.failedBalls.toString(),
            )
            DetailInfoCard(
                label = stringResource(R.string.detail_difficulty),
                value = record.difficultyLabel,
            )
            DetailInfoCard(
                label = stringResource(R.string.detail_date),
                value = record.dateLabel,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.detail_notes_label),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = record.notes.ifBlank {
                            stringResource(R.string.detail_notes_empty)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (record.notes.isBlank()) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }
            }

            Button(
                onClick = onDeleteClick,
                enabled = !uiState.isDeleting,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Text(text = stringResource(R.string.detail_delete_record))
            }
        }
    }
}

@Composable
private fun DetailInfoCard(
    label: String,
    value: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecordDetailScreenPreview() {
    BilliardsTrainingTrackerTheme {
        Surface {
            RecordDetailScreenContent(
                uiState = RecordDetailUiState(
                    record = RecordDetailItem(
                        id = 1,
                        practiceTypeLabel = "拉桿練習",
                        successRate = 60,
                        totalBalls = 20,
                        successBalls = 12,
                        failedBalls = 8,
                        difficultyLabel = "Normal",
                        dateLabel = "2026/05/23",
                        notes = "今天拉桿穩定度不夠，出桿太急。",
                    ),
                ),
                onNavigateBack = {},
                onDeleteClick = {},
                onDismissDeleteDialog = {},
                onConfirmDelete = {},
            )
        }
    }
}
