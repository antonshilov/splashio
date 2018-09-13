package io.github.antonshilov.domain.feed.photos.model

/**
 * Domain model of the Photo for the list of photos
 */
data class Photo(
  val id: String,
  val width: Int,
  val height: Int,
  val color: String,
  val likes: Int,
  val likedByUser: Boolean,
  val description: String,
  val user: User,
  val urls: Urls,
  val links: Links
)
