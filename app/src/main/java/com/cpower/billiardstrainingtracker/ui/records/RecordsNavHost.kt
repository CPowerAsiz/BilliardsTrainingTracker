package com.cpower.billiardstrainingtracker.ui.records

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cpower.billiardstrainingtracker.navigation.RecordsRoutes
import com.cpower.billiardstrainingtracker.ui.records.detail.RecordDetailScreen

@Composable
fun RecordsNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RecordsRoutes.LIST,
        modifier = modifier,
    ) {
        composable(RecordsRoutes.LIST) {
            RecordsScreen(
                onRecordClick = { recordId ->
                    navController.navigate(RecordsRoutes.detail(recordId))
                },
            )
        }
        composable(
            route = RecordsRoutes.DETAIL,
            arguments = listOf(
                navArgument("recordId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments?.getLong("recordId") ?: return@composable
            RecordDetailScreen(
                recordId = recordId,
                onNavigateBack = { navController.popBackStack() },
                onRecordDeleted = { navController.popBackStack() },
            )
        }
    }
}
