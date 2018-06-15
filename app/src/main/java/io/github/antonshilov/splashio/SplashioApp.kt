package io.github.antonshilov.splashio

import android.app.Application
import timber.log.Timber

class SplashioApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())

  }
}