package io.github.antonshilov.splashio

import android.app.Application
import io.github.antonshilov.splashio.di.appModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/**
 * Android application class that performs application-wide configuration
 * Timber logger
 * Koin context initialisation
 */
class SplashioApp : Application() {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    startKoin(listOf(appModule))
  }
}
