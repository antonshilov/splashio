package io.github.antonshilov.domain.feed.photos.model

import java.io.Serializable

data class PhotoLinks(
  val self: String,
  val html: String,
  val download: String
) : Serializable
