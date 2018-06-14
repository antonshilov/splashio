package io.github.antonshilov.splashio.api.model

import com.google.gson.annotations.SerializedName

data class ProfileImage(
  @SerializedName("small") val small: String,
  @SerializedName("medium") val medium: String,
  @SerializedName("large") val large: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(small)
    parcel.writeString(medium)
    parcel.writeString(large)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<ProfileImage> {
    override fun createFromParcel(parcel: Parcel): ProfileImage {
      return ProfileImage(parcel)
    }

    override fun newArray(size: Int): Array<ProfileImage?> {
      return arrayOfNulls(size)
    }
  }
}

data class Photo(
  @SerializedName("id") val id: String,
  @SerializedName("created_at") val createdAt: String,
  @SerializedName("updated_at") val updatedAt: String,
  @SerializedName("width") val width: Int,
  @SerializedName("height") val height: Int,
  @SerializedName("color") val color: String,
  @SerializedName("likes") val likes: Int,
  @SerializedName("liked_by_user") val likedByUser: Boolean,
  @SerializedName("description") val description: String,
  @SerializedName("user") val user: User,
  @SerializedName("urls") val urls: Urls,
  @SerializedName("links") val links: Links
) : Parcelable {
  val url: String
    get() = urls.small

  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readString(),
    parcel.readInt(),
    parcel.readByte() != 0.toByte(),
    parcel.readString(),
    parcel.readParcelable(User::class.java.classLoader),
    parcel.readParcelable(Urls::class.java.classLoader),
    parcel.readParcelable(Links::class.java.classLoader))

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeString(createdAt)
    parcel.writeString(updatedAt)
    parcel.writeInt(width)
    parcel.writeInt(height)
    parcel.writeString(color)
    parcel.writeInt(likes)
    parcel.writeByte(if (likedByUser) 1 else 0)
    parcel.writeString(description)
    parcel.writeParcelable(user, flags)
    parcel.writeParcelable(urls, flags)
    parcel.writeParcelable(links, flags)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Photo> {
    override fun createFromParcel(parcel: Parcel): Photo {
      return Photo(parcel)
    }

    override fun newArray(size: Int): Array<Photo?> {
      return arrayOfNulls(size)
    }
  }
}

data class Links(
  @SerializedName("self") val self: String,
  @SerializedName("html") val html: String,
  @SerializedName("download") val download: String,
  @SerializedName("download_location") val downloadLocation: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(self)
    parcel.writeString(html)
    parcel.writeString(download)
    parcel.writeString(downloadLocation)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Links> {
    override fun createFromParcel(parcel: Parcel): Links {
      return Links(parcel)
    }

    override fun newArray(size: Int): Array<Links?> {
      return arrayOfNulls(size)
    }
  }
}

data class User(
  @SerializedName("id") val id: String,
  @SerializedName("username") val username: String,
  @SerializedName("name") val name: String,
  @SerializedName("portfolio_url") val portfolioUrl: String,
  @SerializedName("bio") val bio: String,
  @SerializedName("location") val location: String,
  @SerializedName("total_likes") val totalLikes: Int,
  @SerializedName("total_photos") val totalPhotos: Int,
  @SerializedName("total_collections") val totalCollections: Int,
  @SerializedName("instagram_username") val instagramUsername: String,
  @SerializedName("twitter_username") val twitterUsername: String,
  @SerializedName("profile_image") val profileImage: ProfileImage,
  @SerializedName("links") val links: Links
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readString(),
    parcel.readString(),
    parcel.readParcelable(ProfileImage::class.java.classLoader),
    parcel.readParcelable(Links::class.java.classLoader))

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeString(username)
    parcel.writeString(name)
    parcel.writeString(portfolioUrl)
    parcel.writeString(bio)
    parcel.writeString(location)
    parcel.writeInt(totalLikes)
    parcel.writeInt(totalPhotos)
    parcel.writeInt(totalCollections)
    parcel.writeString(instagramUsername)
    parcel.writeString(twitterUsername)
    parcel.writeParcelable(profileImage, flags)
    parcel.writeParcelable(links, flags)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<User> {
    override fun createFromParcel(parcel: Parcel): User {
      return User(parcel)
    }

    override fun newArray(size: Int): Array<User?> {
      return arrayOfNulls(size)
    }
  }
}

data class Urls(
  val raw: String,
  val full: String,
  val regular: String,
  val small: String,
  val thumb: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(raw)
    parcel.writeString(full)
    parcel.writeString(regular)
    parcel.writeString(small)
    parcel.writeString(thumb)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Urls> {
    override fun createFromParcel(parcel: Parcel): Urls {
      return Urls(parcel)
    }

    override fun newArray(size: Int): Array<Urls?> {
      return arrayOfNulls(size)
    }
  }
}