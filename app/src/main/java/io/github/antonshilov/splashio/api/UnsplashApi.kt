package io.github.antonshilov.splashio.api

import io.github.antonshilov.splashio.api.model.Photo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface UnsplashApi {
  /**
   * Loads the curated photos.
   *
   * Pagination params
   * @param page number of page to load the photos
   * @param limit amount of photos per page
   *
   * @return http call to request the photos
   */
  @GET("/photos/curated")
  fun getFeed(@Query("per_page") limit: Int = 50, @Query("page") page: Int = 1): Call<List<Photo>>

  companion object {
    private const val BASE_URL = "https://api.unsplash.com/"

    /**
     * Factory method to create the instance of the [UnsplashApi] with a pre-defined base url.
     */
    fun create() = create(BASE_URL)

    /**
     * Factory method to create the instance of the [UnsplashApi]
     * @param baseUrl the base url for the api service
     */
    fun create(baseUrl: String): UnsplashApi {
      val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()
      return Retrofit.Builder()
        .client(httpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UnsplashApi::class.java)
    }
  }
}

