package io.github.antonshilov.remote.mapper

import io.github.antonshilov.domain.model.Photo
import io.github.antonshilov.remote.model.PhotoModel

/**
 * Mapper that maps [PhotoModel] from the data layer to the domain layer [Photo]
 */
open class PhotoEntityMapper : EntityMapper<PhotoModel, Photo> {
  override fun mapFromRemote(model: PhotoModel): Photo {
    return Photo(model.id, model.width, model.height)
  }
}

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <M> the remote model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<in M, out E> {

  /**
   * Maps [M] to the [E]z
   */
  fun mapFromRemote(model: M): E
}