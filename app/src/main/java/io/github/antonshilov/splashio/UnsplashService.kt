package io.github.antonshilov.splashio

import retrofit2.Call
import retrofit2.http.GET
import java.util.*


interface UnsplashService {
    companion object {
        const val ENDPOINT = "https://unsplash.it"
    }

    @GET("/list")
    fun getFeed(): Call<List<Photo>>
}

data class Photo(
        val format: String,
        val width: Int,
        val height: Int,
        val filename: String,
        val id: Long,
        val author: String,
        val author_url: String,
        val post_url: String

) {

    companion object {
        const val PHOTO_URL_BASE = "https://unsplash.it/%d?image=%d"
    }

    fun getPhotoUrl(requestWidth: Int = 150): String {
        return String.format(Locale.getDefault(), PHOTO_URL_BASE, requestWidth, id)
    }
}
