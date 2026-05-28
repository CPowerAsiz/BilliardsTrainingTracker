package com.cpower.billiardstrainingtracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.cpower.billiardstrainingtracker.AppContainer
import com.cpower.billiardstrainingtracker.BilliardsApplication

@Composable
fun rememberAppContainer(): AppContainer {
    val context = LocalContext.current
    return remember {
        (context.applicationContext as BilliardsApplication).container
    }
}
