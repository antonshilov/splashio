package io.github.antonshilov.splashio.di

import io.github.antonshilov.splashio.BuildConfig
import io.github.antonshilov.splashio.api.AuthInterceptor
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.ui.featured.PhotoListViewModel
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val appModule: Module = module {
  viewModel { PhotoListViewModel(get()) }
  viewModel { FullscreenImageViewModel() }

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
