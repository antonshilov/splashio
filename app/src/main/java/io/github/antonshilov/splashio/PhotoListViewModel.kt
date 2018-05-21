package io.github.antonshilov.splashio

class PhotoListViewModel : ViewModel() {

    val photos: MutableLiveData<List<Photo>> = MutableLiveData()
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    fun loadPhotos() {
        loadStatus(true)
        val unsplashApi = Retrofit.Builder()
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