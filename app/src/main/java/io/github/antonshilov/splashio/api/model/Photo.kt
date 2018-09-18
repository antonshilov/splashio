package io.github.antonshilov.splashio.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileImage(
  @SerializedName("small") val small: String,
  @SerializedName("medium") val medium: String,
  @SerializedName("large") val large: String
) : Parcelable

@Parcelize
data class Photo(
  @SerializedName("id") val id: String,
  @SerializedName("width") val width: Int,
  @SerializedName("height") val height: Int,
  @SerializedName("color") val color: String,
  @SerializedName("likes") val likes: Int,
  @SerializedName("liked_by_user") val likedByUser: Boolean,
  @SerializedName("description") val description: String?,
  @SerializedName("user") val user: User?,
  @SerializedName("urls") val urls: Urls,
  @SerializedName("links") val links: Links?
) : Parcelable {
  val url: String
    get() = urls.small
  val name: String
    get() = "${user!!.username}_${id}_splashio"
}

@Parcelize
data class Links(
  @SerializedName("self") val self: String?,
  @SerializedName("html") val html: String?,
  @SerializedName("download") val download: String?,
  @SerializedName("download_location") val downloadLocation: String?
) : Parcelable

@Parcelize
data class User(
  @SerializedName("id") val id: String,
  @SerializedName("username") val username: String?,
  @SerializedName("name") val name: String?,
  @SerializedName("portfolio_url") val portfolioUrl: String?,
  @SerializedName("bio") val bio: String?,
  @SerializedName("location") val location: String?,
  @SerializedName("total_likes") val totalLikes: Int,
  @SerializedName("total_photos") val totalPhotos: Int,
  @SerializedName("total_collections") val totalCollections: Int,
  @SerializedName("instagram_username") val instagramUsername: String?,
  @SerializedName("twitter_username") val twitterUsername: String?,
  @SerializedName("profile_image") val profileImage: ProfileImage?,
  @SerializedName("links") val links: Links?
) : Parcelable

@Parcelize
data class Urls(
  val raw: String,
  val full: String,
  val regular: String,
  val small: String,
  val thumb: String
) : Parcelable
