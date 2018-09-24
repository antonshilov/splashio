package io.github.antonshilov.remote.mapper

import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.domain.feed.photos.model.PhotoLinks
import io.github.antonshilov.domain.feed.photos.model.ProfileImage
import io.github.antonshilov.domain.feed.photos.model.Urls
import io.github.antonshilov.domain.feed.photos.model.UserLinks
import io.github.antonshilov.remote.model.PhotoModel
import io.github.antonshilov.remote.model.User

/**
 * Mapper that maps [PhotoModel] from the data layer to the domain layer [Photo]
 */
open class PhotoEntityMapper(
  private val userEntityMapper: UserEntityMapper,
  private val urlMapper: UrlMapper, private val linksMapper: PhotoLinksMapper
) : EntityMapper<PhotoModel, Photo> {

  override fun mapFromRemote(model: PhotoModel): Photo {
    return Photo(
      model.id,
      model.width,
      model.height,
      model.color,
      model.likes,
      model.likedByUser,
      model.description,
      userEntityMapper.mapFromRemote(model.user),
      urlMapper.mapFromRemote(model.urls),
      linksMapper.mapFromRemote(model.links)
    )
  }
}

open class UserEntityMapper(
  private val profileImageMapper: ProfileImageMapper,
  private val userLinksMapper: UserLinksMapper
) : EntityMapper<User, io.github.antonshilov.domain.feed.photos.model.User> {

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
      profileImageMapper.mapFromRemote(model.profileImage),
      userLinksMapper.mapFromRemote(model.links)
    )
  }
}

class ProfileImageMapper : EntityMapper<io.github.antonshilov.remote.model.ProfileImage, ProfileImage> {
  override fun mapFromRemote(model: io.github.antonshilov.remote.model.ProfileImage): ProfileImage {
    return with(model) {
      ProfileImage(small, medium, large)
    }
  }
}

class UrlMapper : EntityMapper<io.github.antonshilov.remote.model.Urls, Urls> {
  override fun mapFromRemote(model: io.github.antonshilov.remote.model.Urls): Urls {
    return with(model) {
      Urls(raw, full, regular, small, thumb)
    }
  }
}

class UserLinksMapper : EntityMapper<io.github.antonshilov.remote.model.UserLinks, UserLinks> {
  override fun mapFromRemote(model: io.github.antonshilov.remote.model.UserLinks): UserLinks {
    return with(model) {
      UserLinks(self, html, photos, likes, portfolio)
    }
  }
}

class PhotoLinksMapper : EntityMapper<io.github.antonshilov.remote.model.PhotoLinks, PhotoLinks> {
  override fun mapFromRemote(model: io.github.antonshilov.remote.model.PhotoLinks): PhotoLinks {
    return with(model) {
      PhotoLinks(self, html, download)
    }
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
