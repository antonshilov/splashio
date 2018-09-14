package io.github.antonshilov.remote.model

import com.google.gson.annotations.SerializedName

data class ProfileImage(val small: String, val medium: String, val large: String)

data class PhotoModel(
  val id: String,
  val width: Int,
  val height: Int,
  val color: String,
  val likes: Int,
  @SerializedName("liked_by_user") val likedByUser: Boolean,
  val description: String,
  val user: User,
  val urls: Urls,
  val links: PhotoLinks
) {
  val url: String
    get() = urls.small
  val name: String
    get() = "${user.username}_${id}_splashio"
}

data class PhotoLinks(
  val self: String,
  val html: String,
  val download: String,
  @SerializedName("download_location") val downloadLocation: String
)

data class User(
  val id: String,
  val username: String,
  val name: String,
  @SerializedName("portfolio_url") val portfolioUrl: String?,
  val bio: String?,
  val location: String?,
  @SerializedName("total_likes") val totalLikes: Int,
  @SerializedName("total_photos") val totalPhotos: Int,
  @SerializedName("total_collections") val totalCollections: Int,
  @SerializedName("instagram_username") val instagramUsername: String?,
  @SerializedName("twitter_username") val twitterUsername: String?,
  @SerializedName("profile_image") val profileImage: ProfileImage,
  val links: UserLinks
)

data class UserLinks(
  val self: String,
  val html: String,
  val photos: String,
  val likes: String,
  val portfolio: String
)

data class Urls(
  val raw: String,
  val full: String,
  val regular: String,
  val small: String,
  val thumb: String
)
