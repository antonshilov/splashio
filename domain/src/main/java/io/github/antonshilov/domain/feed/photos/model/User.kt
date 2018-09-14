package io.github.antonshilov.domain.feed.photos.model

data class User(
  val id: String,
  val username: String,
  val name: String,
  val portfolioUrl: String?,
  val bio: String?,
  val location: String?,
  val totalLikes: Int,
  val totalPhotos: Int,
  val totalCollections: Int,
  val profileImage: ProfileImage?,
  val userLinks: UserLinks
)
