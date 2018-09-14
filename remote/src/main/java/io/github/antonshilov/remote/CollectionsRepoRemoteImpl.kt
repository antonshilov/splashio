package io.github.antonshilov.remote

import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.domain.feed.collections.CollectionsRepo
import io.github.antonshilov.remote.mapper.CollectionEntityMapper
import io.github.antonshilov.remote.service.UnsplashApi
import io.reactivex.Observable

class CollectionsRepoRemoteImpl(private val api: UnsplashApi, private val entityMapper: CollectionEntityMapper) :
  CollectionsRepo {
  override fun getCollections(page: Int, pageSize: Int): Observable<List<Collection>> {
    return api.getLatestCollections(page, pageSize).map {
      it.map {
        entityMapper.mapFromRemote(it)
      }
    }
  }
}
