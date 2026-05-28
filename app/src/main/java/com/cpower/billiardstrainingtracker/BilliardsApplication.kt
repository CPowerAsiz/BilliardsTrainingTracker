package com.cpower.billiardstrainingtracker

import android.app.Application

class BilliardsApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
