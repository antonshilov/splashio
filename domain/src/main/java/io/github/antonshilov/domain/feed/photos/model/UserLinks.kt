package io.github.antonshilov.domain.feed.photos.model

import java.io.Serializable

data class UserLinks(
  val self: String,
  val html: String,
  val photos: String,
  val likes: String,
  val portfolio: String
) : Serializable
