package io.github.antonshilov.remote.mapper

import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.remote.model.CollectionModel

class CollectionEntityMapper(private val photoMapper: PhotoEntityMapper) : EntityMapper<CollectionModel, Collection> {
  override fun mapFromRemote(model: CollectionModel): Collection {
    return with(model) {
      Collection(
        id,
        title,
        description,
        publishedAt,
        updatedAt,
        curated,
        totalPhotos,
        private,
        shareKey,
        photoMapper.mapFromRemote(coverPhoto)
      )
    }
  }
}
