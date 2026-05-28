package com.cpower.billiardstrainingtracker.navigation

object RecordsRoutes {
    const val LIST = "records_list"
    const val DETAIL = "record_detail/{recordId}"

    fun detail(recordId: Long): String = "record_detail/$recordId"
}
