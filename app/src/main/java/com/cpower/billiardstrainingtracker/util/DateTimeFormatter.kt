package com.cpower.billiardstrainingtracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeFormatter {

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

    fun formatDate(epochMillis: Long): String =
        dateFormat.format(Date(epochMillis))

    fun formatDateTime(epochMillis: Long): String =
        dateTimeFormat.format(Date(epochMillis))
}
