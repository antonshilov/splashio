package io.github.antonshilov.remote.service

import io.github.antonshilov.remote.model.CollectionModel
import io.github.antonshilov.remote.model.PhotoModel
import io.reactivex.Observable
import io.reactivex.Single
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
   * @param page number of page to load the items
   * @param pageSize amount of items per page
   *
   * @return [Observable] that emits the page of photo items
   */
  @GET("/photos")
  fun getLatestPhotos(@Query("page") page: Int, @Query("per_page") pageSize: Int): Single<List<PhotoModel>>
  // TODO use [Url] parameter annotation to unify new/feed/curated

  /**
   * Request API to retrieve the latest collections
   *
   * Pagination params
   * @param page number of page to load the items
   * @param pageSize amount of items per page
   *
   * @return [Observable] that emits the page of collection items
   */
  @GET("/collections")
  fun getLatestCollections(@Query("page") page: Int, @Query("per_page") pageSize: Int): Single<List<CollectionModel>>
}
