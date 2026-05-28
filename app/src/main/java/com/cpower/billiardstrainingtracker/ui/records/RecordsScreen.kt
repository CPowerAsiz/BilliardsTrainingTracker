package com.cpower.billiardstrainingtracker.ui.records

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.cpower.billiardstrainingtracker.R
import com.cpower.billiardstrainingtracker.ui.rememberAppContainer
import com.cpower.billiardstrainingtracker.ui.theme.BilliardsTrainingTrackerTheme

@Composable
fun RecordsScreen(
    onRecordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel = viewModel(
        factory = RecordsViewModelFactory(rememberAppContainer().practiceRepository),
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecordsScreenContent(
        uiState = uiState,
        onRecordClick = onRecordClick,
        modifier = modifier,
    )
}

@Composable
fun RecordsScreenContent(
    uiState: RecordsUiState,
    onRecordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.groupedRecords.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.records_empty),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.records_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        uiState.groupedRecords.forEach { group ->
            item(key = "date_${group.dateLabel}") {
                Text(
                    text = group.dateLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                )
            }
            items(group.records, key = { it.id }) { record ->
                RecordCard(
                    record = record,
                    onClick = { onRecordClick(record.id) },
                )
            }
        }
    }
}

@Composable
private fun RecordCard(
    record: RecordListItem,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = record.practiceTypeLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.records_item_success_rate, record.successRate),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(
                    R.string.records_item_balls,
                    record.totalBalls,
                    record.successBalls,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.records_item_difficulty, record.difficultyLabel),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecordsScreenPreview() {
    BilliardsTrainingTrackerTheme {
        Surface {
            RecordsScreenContent(
                uiState = RecordsUiState(
                    groupedRecords = listOf(
                        RecordDateGroup(
                            dateLabel = "2026/05/23",
                            records = listOf(
                                RecordListItem(
                                    id = 1,
                                    practiceTypeLabel = "拉桿練習",
                                    totalBalls = 20,
                                    successBalls = 12,
                                    successRate = 60,
                                    difficultyLabel = "Normal",
                                    practicedAtMillis = 0L,
                                ),
                            ),
                        ),
                        RecordDateGroup(
                            dateLabel = "2026/05/22",
                            records = listOf(
                                RecordListItem(
                                    id = 2,
                                    practiceTypeLabel = "高低桿控制",
                                    totalBalls = 20,
                                    successBalls = 9,
                                    successRate = 45,
                                    difficultyLabel = "Hard",
                                    practicedAtMillis = 0L,
                                ),
                            ),
                        ),
                    ),
                ),
                onRecordClick = {},
            )
        }
    }
}
