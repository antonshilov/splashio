package io.github.antonshilov.splashio.di

import io.github.antonshilov.splashio.BuildConfig
import io.github.antonshilov.splashio.api.AuthInterceptor
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.ui.featured.PhotoListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val appModule: Module = applicationContext {
  viewModel { PhotoListViewModel(get()) }

  bean { provideOkHttpClient() }
  bean { UnsplashApi.create(client = get()) }
}

/**
 * @return [OkHttpClient] instance with the [AuthInterceptor] and [HttpLoggingInterceptor] configured
 */
fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .addInterceptor(HttpLoggingInterceptor().apply { if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BASIC })
    .build()