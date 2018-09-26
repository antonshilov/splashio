package io.github.antonshilov.domain.feed.collections

import io.github.antonshilov.domain.feed.photos.model.Photo

data class Collection(
  val id: Int,
  val title: String,
  val description: String?,
  val publishedAt: String,
  val updatedAt: String,
  val curated: Boolean,
  val totalPhotos: Int,
  val private: Boolean,
  val shareKey: String,
  val coverPhoto: Photo,
  val previewPhotosUrls: List<String>
)
