package io.github.antonshilov.domain.feed.photos.model

import java.io.Serializable

data class Urls(
  val raw: String,
  val full: String,
  val regular: String,
  val small: String,
  val thumb: String
) : Serializable
