package com.example.weatherapplication

import android.app.Application
import timber.log.Timber

class WeatherApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}