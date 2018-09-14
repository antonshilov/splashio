package io.github.antonshilov.remote.service

import io.github.antonshilov.remote.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class is responsible for the creation the instance of the [UnsplashApi]
 */
object UnsplashApiFactory {
  private const val BASE_URL = "https://api.unsplash.com/"

  /**
   * Factory method to create the instance of the [UnsplashApi]
   * @param isDebug indicates if application is running in debug mode
   */
  fun createUnsplashApi(isDebug: Boolean): UnsplashApi {
    val client = createOkHttpClient(createLoggingInterceptor(isDebug))
    return Retrofit.Builder()
      .client(client)
      .baseUrl(BASE_URL)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(UnsplashApi::class.java)
  }

  /**
   * @return [OkHttpClient] instance with the [AuthInterceptor] and [HttpLoggingInterceptor] configured
   */
  private fun createOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .addInterceptor(httpLoggingInterceptor)
    .build()

  /**
   * @return [HttpLoggingInterceptor] which logs http requests for the debug build
   */
  private fun createLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
      level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
  }
}
