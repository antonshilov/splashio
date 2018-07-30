package io.github.antonshilov.remote

import io.github.antonshilov.domain.PhotoRepo
import io.github.antonshilov.domain.model.Photo
import io.github.antonshilov.remote.mapper.PhotoEntityMapper
import io.github.antonshilov.remote.service.UnsplashApi
import io.reactivex.Observable

/**
 * The implementation of [PhotoRepo] that calls remote [UnsplashApi] to retrieve the data.
 */
class PhotoRemoteImpl(private val unsplashApi: UnsplashApi, private val entityMapper: PhotoEntityMapper) : PhotoRepo {
  override fun getLatestPhotos(): Observable<List<Photo>> {
    return unsplashApi.getLatestPhotos()
      .map {
        it.map { entityMapper.mapFromRemote(it) }
      }
  }
}