package io.github.antonshilov.remote.model

import com.google.gson.annotations.SerializedName

data class CollectionModel(
  val id: Int,
  val title: String,
  val description: String,
  @SerializedName("published_at")
  val publishedAt: String,
  @SerializedName("updated_at")
  val updatedAt: String,
  val curated: Boolean,
  @SerializedName("total_photos")
  val totalPhotos: Int,
  val private: Boolean,
  @SerializedName("share_key")
  val shareKey: String,
  @SerializedName("cover_photo")
  val coverPhoto: PhotoModel,
  @SerializedName("preview_photos")
  val previewPhotos: List<PreviewPhoto>?,
  val user: User?
)

data class PreviewPhoto(
  val id: Int,
  val urls: Urls
)
