package io.github.antonshilov.domain.feed.photos.model

import java.io.Serializable

data class ProfileImage(
  val small: String,
  val medium: String,
  val large: String
) : Serializable
