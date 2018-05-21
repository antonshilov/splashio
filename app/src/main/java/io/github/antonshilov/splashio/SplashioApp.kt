package io.github.antonshilov.splashio

import android.app.Application

class SplashioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }
}