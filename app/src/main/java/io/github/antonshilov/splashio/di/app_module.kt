package io.github.antonshilov.splashio.di

import io.github.antonshilov.domain.feed.collections.GetCollections
import io.github.antonshilov.domain.feed.photos.GetPhotos
import io.github.antonshilov.remote.AuthInterceptor
import io.github.antonshilov.remote.CollectionsRepoRemoteImpl
import io.github.antonshilov.remote.PhotoRemoteImpl
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
import io.github.antonshilov.splashio.ui.collections.CollectionListViewModel
import io.github.antonshilov.splashio.ui.collections.CollectionsAdapter
import io.github.antonshilov.splashio.ui.featured.PhotoListViewModel
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  viewModel { PhotoListViewModel(get(), AndroidSchedulers.mainThread()) }
  viewModel { CollectionListViewModel(get()) }

  factory { GetCollections(get(), get()) }
  factory { CollectionsAdapter() }
  factory { GetPhotos(get(), get()) }
  factory { PaginationStateMachine(get()) }

  single { UiThread() }
  factory { provideOkHttpClient() }
  single { UnsplashApiFactory.createUnsplashApi(BuildConfig.DEBUG) }
  single { PhotoRemoteImpl(get(), get()) }
  single {
    PhotoEntityMapper(
      UserEntityMapper(
        ProfileImageMapper(),
        UserLinksMapper()
      ),
      UrlMapper(),
      PhotoLinksMapper()
    )
  }
  single {
    CollectionsRepoRemoteImpl(get(), CollectionEntityMapper(get()))
  }
}

/**
 * @return [OkHttpClient] instance with the [AuthInterceptor] and [HttpLoggingInterceptor] configured
 */
fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
  .addInterceptor(AuthInterceptor())
  .addInterceptor(HttpLoggingInterceptor().apply { if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BASIC })
  .build()
