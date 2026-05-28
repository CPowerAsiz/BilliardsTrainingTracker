package com.cpower.billiardstrainingtracker.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun HomeScreen(
    onStartPractice: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(rememberAppContainer().practiceRepository),
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onStartPractice = onStartPractice,
        modifier = modifier,
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onStartPractice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.home_app_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        HomeSectionCard(title = stringResource(R.string.home_today_practice_title)) {
            val statusText = if (uiState.hasPracticedToday) {
                stringResource(R.string.home_today_practice_completed)
            } else {
                stringResource(R.string.home_today_practice_not_completed)
            }
            val statusColor = if (uiState.hasPracticedToday) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = statusColor,
            )
        }

        HomeSectionCard(title = stringResource(R.string.home_weekly_stats_title)) {
            Text(
                text = stringResource(
                    R.string.home_weekly_practice_count,
                    uiState.weeklyPracticeCount,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(
                    R.string.home_weekly_average_success_rate,
                    uiState.weeklyAverageSuccessRate,
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        HomeSectionCard(title = stringResource(R.string.home_recent_practice_title)) {
            val lastPractice = uiState.lastPractice
            if (lastPractice != null) {
                Text(
                    text = stringResource(
                        R.string.home_recent_practice_detail,
                        lastPractice.drillName,
                        lastPractice.totalBalls,
                        lastPractice.successBalls,
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                Text(
                    text = stringResource(R.string.home_no_recent_practice),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        HomeSectionCard(title = stringResource(R.string.home_weakest_item_title)) {
            val weakestItem = uiState.weakestItem
            if (weakestItem != null) {
                Text(
                    text = stringResource(
                        R.string.home_weakest_item_detail,
                        weakestItem.name,
                        weakestItem.successRate,
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                Text(
                    text = stringResource(R.string.home_no_weakest_item),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Button(
            onClick = onStartPractice,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.home_start_practice))
        }
    }
}

@Composable
private fun HomeSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            content()
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    BilliardsTrainingTrackerTheme {
        Surface {
            HomeScreenContent(
                uiState = previewHomeUiState(),
                onStartPractice = {},
            )
        }
    }
}
