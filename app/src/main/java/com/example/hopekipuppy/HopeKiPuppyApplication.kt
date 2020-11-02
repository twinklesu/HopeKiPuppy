package com.example.hopekipuppy

import android.app.Application
import timber.log.Timber

class HopeKiPuppyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}