package io.github.antonshilov.splashio

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.antonshilov.splashio.api.AuthInterceptor
import io.github.antonshilov.splashio.api.Photo
import io.github.antonshilov.splashio.api.UnsplashService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoListViewModel : ViewModel() {

  val photos: MutableLiveData<List<Photo>> = MutableLiveData()
  val loader: MutableLiveData<Boolean> = MutableLiveData()

  fun loadPhotos() {
    loadStatus(true)
    val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
    val unsplashApi = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(UnsplashService.ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UnsplashService::class.java)
    val call = unsplashApi.getFeed()
    call.enqueue(object : Callback<List<Photo>> {
      override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
        loadStatus(false)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }

      override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
        loadStatus(false)
        val images = response.body()!!
        photos.value = images
      }

    })
  }

  private fun loadStatus(isLoading: Boolean) {
    loader.value = isLoading
  }

}