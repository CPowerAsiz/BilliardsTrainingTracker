package com.cpower.billiardstrainingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cpower.billiardstrainingtracker.ui.home.HomeScreen
import com.cpower.billiardstrainingtracker.ui.practice.PracticeScreen
import com.cpower.billiardstrainingtracker.ui.records.RecordsNavHost
import com.cpower.billiardstrainingtracker.ui.rememberAppContainer
import com.cpower.billiardstrainingtracker.ui.settings.SettingsScreen
import com.cpower.billiardstrainingtracker.ui.settings.SettingsViewModel
import com.cpower.billiardstrainingtracker.ui.settings.SettingsViewModelFactory
import com.cpower.billiardstrainingtracker.ui.theme.BilliardsTrainingTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BilliardsTrainingTrackerApp()
        }
    }
}

@Composable
fun BilliardsTrainingTrackerApp() {
    val container = rememberAppContainer()
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            userPreferencesRepository = container.userPreferencesRepository,
            practiceRepository = container.practiceRepository,
            appVersion = BuildConfig.VERSION_NAME,
        ),
    )
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    BilliardsTrainingTrackerTheme(darkTheme = settingsState.isDarkMode) {
        BilliardsTrainingTrackerContent()
    }
}

@Composable
private fun BilliardsTrainingTrackerContent() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(destination.labelResId),
                        )
                    },
                    label = { Text(stringResource(destination.labelResId)) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination },
                )
            }
        },
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(
                    onStartPractice = { currentDestination = AppDestinations.PRACTICE },
                    modifier = Modifier.padding(innerPadding),
                )
                AppDestinations.PRACTICE -> PracticeScreen(
                    onRecordSaved = { currentDestination = AppDestinations.RECORDS },
                    modifier = Modifier.padding(innerPadding),
                )
                AppDestinations.RECORDS -> RecordsNavHost(
                    modifier = Modifier.padding(innerPadding),
                )
                AppDestinations.SETTINGS -> SettingsScreen(
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

enum class AppDestinations(
    val labelResId: Int,
    val icon: ImageVector,
) {
    HOME(R.string.nav_home, Icons.Default.Home),
    PRACTICE(R.string.nav_practice, Icons.Default.AddCircle),
    RECORDS(R.string.nav_records, Icons.AutoMirrored.Filled.List),
    SETTINGS(R.string.nav_settings, Icons.Default.Settings),
}

@Preview
@Composable
private fun BilliardsTrainingTrackerAppPreview() {
    BilliardsTrainingTrackerTheme {
        BilliardsTrainingTrackerContent()
    }
}
