package io.github.antonshilov.splashio.api

import io.github.antonshilov.splashio.api.model.Photo
import okhttp3.OkHttpClient
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
  fun getCuratedPhotos(@Query("per_page") limit: Int = 50, @Query("page") page: Int = 1): Call<List<Photo>>

  companion object {
    private const val BASE_URL = "https://api.unsplash.com/"

    /**
     * Factory method to create the instance of the [UnsplashApi]
     * @param baseUrl the base url for the api service*/
    fun create(baseUrl: String = BASE_URL, client: OkHttpClient): UnsplashApi {
      return Retrofit.Builder()
          .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UnsplashApi::class.java)
    }
  }
}

