package io.github.antonshilov.remote.service

import io.github.antonshilov.remote.model.PhotoModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service that describes methods of the [Unsplash public api](https://unsplash.com/documentation)
 */
interface UnsplashApi {
  /**
   * Loads the curated photos.
   *
   * Pagination params
   * @param page number of page to load the photos
   * @param pageSize amount of photos per page
   *
   * @return http call to request the photos
   */
  @GET("/photos")
  fun getLatestPhotos(@Query("page") page: Int, @Query("per_page") pageSize: Int): Observable<List<PhotoModel>>
}
