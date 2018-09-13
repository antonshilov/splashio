package io.github.antonshilov.domain.feed.collections

import io.reactivex.Observable

interface CollectionsRepo {
  /**
   * @param page page number for the pagination query
   * @param pageSize the number of items returned per page
   * @return the list of latest collections uploaded by all users
   */
  fun getCollections(page: Int, pageSize: Int): Observable<List<Collection>>
}
