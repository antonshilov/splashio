package io.github.antonshilov.splashio

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
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
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Fabric.with(this, Crashlytics())
    }
    startKoin(listOf(appModule))
  }
}
