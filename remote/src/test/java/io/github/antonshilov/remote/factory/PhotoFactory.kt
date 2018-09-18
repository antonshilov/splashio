package io.github.antonshilov.remote.factory

import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.remote.factory.DataFactory.randomBoolean
import io.github.antonshilov.remote.factory.DataFactory.randomInt
import io.github.antonshilov.remote.factory.DataFactory.randomUuid
import io.github.antonshilov.remote.model.PhotoLinks
import io.github.antonshilov.remote.model.PhotoModel
import io.github.antonshilov.remote.model.ProfileImage
import io.github.antonshilov.remote.model.Urls
import io.github.antonshilov.remote.model.User

object PhotoFactory {

  fun makePhotoModel(): PhotoModel {
    return PhotoModel(
      randomUuid(),
      randomInt(),
      randomInt(),
      randomUuid(),
      randomInt(),
      randomBoolean(),
      randomUuid(),
      makeUser(),
      makeUrls(),
      makeLinks()
    )
  }

  private fun makeUrls(): Urls {
    return Urls(randomUuid(), randomUuid(), randomUuid(), randomUuid(), randomUuid())
  }

  private fun makeLinks(): PhotoLinks {
    return PhotoLinks(randomUuid(), randomUuid(), randomUuid(), randomUuid())
  }

  private fun makeUser(): User {
    return User(
      randomUuid(),
      randomUuid(),
      randomUuid(),
      randomUuid(),
      randomUuid(),
      randomUuid(),
      randomInt(),
      randomInt(),
      randomInt(),
      randomUuid(),
      randomUuid(),
      makeProfileImage(),
      makeUserLinks()
    )
  }

  private fun makeUserLinks(): io.github.antonshilov.remote.model.UserLinks {
    return io.github.antonshilov.remote.model.UserLinks(
      randomUuid(), randomUuid(), randomUuid(), randomUuid(),
      randomUuid()
    )
  }

  private fun makeProfileImage(): ProfileImage {
    return ProfileImage(randomUuid(), randomUuid(), randomUuid())
  }

  fun makePhotoList(): List<PhotoModel> {
    return listOf(makePhotoModel(), makePhotoModel(), makePhotoModel())
  }

  fun makePhotoEntity(): Photo {
    return Photo(
      randomUuid(), randomInt(), randomInt(), randomUuid(), randomInt(), randomBoolean(), randomUuid(),
      randomUuid(), makeUser(), makeLinks()
    )
  }
}
