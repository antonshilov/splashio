package io.github.antonshilov.splashio.api

import android.os.Parcel
import android.os.Parcelable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface UnsplashService {
  companion object {
    const val ENDPOINT = "https://api.unsplash.com/"

  }

  @GET("/photos/curated")
  fun getFeed(@Query("per_page") limit: Int = 50): Call<List<Photo>>
}

data class Photo(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val width: Int,
    val height: Int,
    val color: String,
    val downloads: Int,
    val likes: Int,
    val liked_by_user: Boolean,
    val description: String,
    val urls: Urls
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
      parcel.readInt(),
      parcel.readByte() != 0.toByte(),
      parcel.readString(),
      parcel.readParcelable(Urls::class.java.classLoader))

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeString(created_at)
    parcel.writeString(updated_at)
    parcel.writeInt(width)
    parcel.writeInt(height)
    parcel.writeString(color)
    parcel.writeInt(downloads)
    parcel.writeInt(likes)
    parcel.writeByte(if (liked_by_user) 1 else 0)
    parcel.writeString(description)
    parcel.writeParcelable(urls, flags)
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