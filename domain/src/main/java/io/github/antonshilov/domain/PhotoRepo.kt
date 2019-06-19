package io.github.antonshilov.domain

import io.github.antonshilov.domain.feed.photos.model.Photo
import io.reactivex.Single

/**
 * [Photo] repository which defines what can be done with the photos for other layers to implement
 */
interface PhotoRepo {
  /**
   * @param page page number for the pagination query
   * @param pageSize the number of photos returned per page
   * @return the list of latest photos uploaded by all users
   */
  fun getLatestPhotos(page: Int, pageSize: Int): Single<List<Photo>>
}
