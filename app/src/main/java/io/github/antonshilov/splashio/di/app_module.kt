package io.github.antonshilov.splashio.di

import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.collections.CollectionsRepo
import io.github.antonshilov.domain.feed.collections.GetCollections
import io.github.antonshilov.remote.CollectionsRepoRemoteImpl
import io.github.antonshilov.remote.mapper.CollectionEntityMapper
import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import io.github.antonshilov.remote.mapper.PhotoLinksMapper
import io.github.antonshilov.remote.mapper.ProfileImageMapper
import io.github.antonshilov.remote.mapper.UrlMapper
import io.github.antonshilov.remote.mapper.UserEntityMapper
import io.github.antonshilov.remote.mapper.UserLinksMapper
import io.github.antonshilov.remote.service.UnsplashApiFactory
import io.github.antonshilov.splashio.BuildConfig
import io.github.antonshilov.splashio.UiThread
import io.github.antonshilov.splashio.api.AuthInterceptor
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.ui.collections.CollectionListViewModel
import io.github.antonshilov.splashio.ui.collections.CollectionsAdapter
import io.github.antonshilov.splashio.ui.featured.PhotoListViewModel
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  viewModel { PhotoListViewModel(get()) }
  viewModel { FullscreenImageViewModel() }
  viewModel { CollectionListViewModel(get()) }

  factory { GetCollections(get(), get()) }
  factory { CollectionsAdapter() }

  single { UiThread() as PostExecutionThread }
  factory { provideOkHttpClient() }
  factory { UnsplashApi.create(client = get()) }

  single { UnsplashApiFactory.createUnsplashApi(BuildConfig.DEBUG) }
  single {
    CollectionsRepoRemoteImpl(
      get(), CollectionEntityMapper(
        PhotoEntityMapper(
          UserEntityMapper(
            ProfileImageMapper(),
            UserLinksMapper()
          ),
          UrlMapper(),
          PhotoLinksMapper()
        )
      )
    ) as CollectionsRepo
  }
}

/**
 * @return [OkHttpClient] instance with the [AuthInterceptor] and [HttpLoggingInterceptor] configured
 */
fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
  .addInterceptor(AuthInterceptor())
  .addInterceptor(HttpLoggingInterceptor().apply { if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BASIC })
  .build()
