package com.cpower.billiardstrainingtracker.ui.practice

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cpower.billiardstrainingtracker.R
import com.cpower.billiardstrainingtracker.data.model.Difficulty
import com.cpower.billiardstrainingtracker.data.model.PracticeType
import com.cpower.billiardstrainingtracker.ui.rememberAppContainer
import com.cpower.billiardstrainingtracker.ui.theme.BilliardsTrainingTrackerTheme
import com.cpower.billiardstrainingtracker.util.DateTimeFormatter

@Composable
fun PracticeScreen(
    onRecordSaved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val container = rememberAppContainer()
    val viewModel: PracticeViewModel = viewModel(
        factory = PracticeViewModelFactory(container.practiceRepository),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.recordSaved.collect {
            onRecordSaved()
        }
    }

    PracticeScreenContent(
        uiState = uiState,
        onPracticeTypeChange = viewModel::onPracticeTypeChange,
        onPracticeTypeMenuExpandedChange = viewModel::onPracticeTypeMenuExpandedChange,
        onTotalBallsChange = viewModel::onTotalBallsChange,
        onSuccessBallsChange = viewModel::onSuccessBallsChange,
        onDifficultyChange = viewModel::onDifficultyChange,
        onNotesChange = viewModel::onNotesChange,
        onSaveRecord = viewModel::saveRecord,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreenContent(
    uiState: PracticeUiState,
    onPracticeTypeChange: (PracticeType) -> Unit,
    onPracticeTypeMenuExpandedChange: (Boolean) -> Unit,
    onTotalBallsChange: (String) -> Unit,
    onSuccessBallsChange: (String) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveRecord: () -> Unit,
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
            text = stringResource(R.string.practice_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        FormLabel(text = stringResource(R.string.practice_type_label))
        ExposedDropdownMenuBox(
            expanded = uiState.practiceTypeMenuExpanded,
            onExpandedChange = onPracticeTypeMenuExpandedChange,
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = uiState.practiceType.label,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.practiceTypeMenuExpanded) },
            )
            ExposedDropdownMenu(
                expanded = uiState.practiceTypeMenuExpanded,
                onDismissRequest = { onPracticeTypeMenuExpandedChange(false) },
            ) {
                PracticeType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.label) },
                        onClick = { onPracticeTypeChange(type) },
                    )
                }
            }
        }

        FormLabel(text = stringResource(R.string.practice_total_balls_label))
        OutlinedTextField(
            value = uiState.totalBalls,
            onValueChange = onTotalBallsChange,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.totalBallsError != null,
            supportingText = uiState.totalBallsError?.let { error ->
                { Text(stringResource(error.toMessageRes())) }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        FormLabel(text = stringResource(R.string.practice_success_balls_label))
        OutlinedTextField(
            value = uiState.successBalls,
            onValueChange = onSuccessBallsChange,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.successBallsError != null,
            supportingText = uiState.successBallsError?.let { error ->
                { Text(stringResource(error.toMessageRes())) }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        FormLabel(text = stringResource(R.string.practice_difficulty_label))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            Difficulty.entries.forEachIndexed { index, difficulty ->
                SegmentedButton(
                    selected = uiState.difficulty == difficulty,
                    onClick = { onDifficultyChange(difficulty) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = Difficulty.entries.size,
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(difficulty.label)
                }
            }
        }

        FormLabel(text = stringResource(R.string.practice_notes_label))
        OutlinedTextField(
            value = uiState.notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.practice_notes_placeholder)) },
            minLines = 3,
            maxLines = 5,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.practice_datetime_label),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = uiState.practicedAtFormatted,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        Button(
            onClick = onSaveRecord,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.practice_save_record))
        }
    }
}

private fun PracticeFieldError.toMessageRes(): Int = when (this) {
    PracticeFieldError.INVALID_TOTAL -> R.string.practice_error_total_balls
    PracticeFieldError.INVALID_SUCCESS -> R.string.practice_error_success_balls
    PracticeFieldError.SUCCESS_EXCEEDS_TOTAL -> R.string.practice_error_success_exceeds_total
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PracticeScreenPreview() {
    val practicedAt = System.currentTimeMillis()
    BilliardsTrainingTrackerTheme {
        Surface {
            PracticeScreenContent(
                uiState = PracticeUiState(
                    practicedAtMillis = practicedAt,
                    practicedAtFormatted = DateTimeFormatter.formatDateTime(practicedAt),
                ),
                onPracticeTypeChange = {},
                onPracticeTypeMenuExpandedChange = {},
                onTotalBallsChange = {},
                onSuccessBallsChange = {},
                onDifficultyChange = {},
                onNotesChange = {},
                onSaveRecord = {},
            )
        }
    }
}
