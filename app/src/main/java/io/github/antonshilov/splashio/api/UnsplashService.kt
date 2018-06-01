package io.github.antonshilov.splashio.api

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
) {
  val url: String
    get() = urls.small
}

data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)