package io.github.antonshilov.remote.mapper

import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.domain.feed.photos.model.ProfileImage
import io.github.antonshilov.remote.model.PhotoModel
import io.github.antonshilov.remote.model.User

/**
 * Mapper that maps [PhotoModel] from the data layer to the domain layer [Photo]
 */
open class PhotoEntityMapper : EntityMapper<PhotoModel, Photo> {
  override fun mapFromRemote(model: PhotoModel): Photo {
    return Photo(
      model.id,
      model.width,
      model.height,
      model.color,
      model.likes,
      model.likedByUser,
      model.description,
      UserEntityMapper().mapFromRemote(model.user),

      )
  }
}

open class UserEntityMapper : EntityMapper<User, io.github.antonshilov.domain.feed.photos.model.User> {
  override fun mapFromRemote(model: User): io.github.antonshilov.domain.feed.photos.model.User {
    return io.github.antonshilov.domain.feed.photos.model.User(
      model.id,
      model.username,
      model.name,
      model.portfolioUrl,
      model.bio,
      model.location,
      model.totalLikes,
      model.totalPhotos,
      model.totalCollections,
      ProfileImageMapper().mapFromRemote(model.profileImage),
      Lin
    )
  }
}

class ProfileImageMapper : EntityMapper<io.github.antonshilov.remote.model.ProfileImage, ProfileImage> {
  override fun mapFromRemote(model: io.github.antonshilov.remote.model.ProfileImage): ProfileImage {
  }
}

class ProfileImageMapper : EntityMapper<io.github.antonshilov.remote.model.ProfileImage, ProfileImage> {
  override fun mapFromRemote(model: io.github.antonshilov.remote.model.ProfileImage): ProfileImage {
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
