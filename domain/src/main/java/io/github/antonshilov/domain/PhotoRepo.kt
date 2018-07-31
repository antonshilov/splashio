package io.github.antonshilov.domain

import io.github.antonshilov.domain.model.Photo
import io.reactivex.Observable

/**
 * [Photo] repository which defines what can be done with the photos for other layers to implement
 */
interface PhotoRepo {
  /**
   * @return the list of latest photos uploaded by all users
   */
  fun getLatestPhotos(): Observable<List<Photo>>
}
