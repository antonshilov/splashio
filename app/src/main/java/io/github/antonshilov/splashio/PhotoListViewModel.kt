package io.github.antonshilov.splashio

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.api.model.Photo

/**
 *
 */
class PhotoListViewModel : ViewModel() {
  val photoList = MutableLiveData<PagedList<Photo>>()
  fun loadPhotos() {
    val api = UnsplashApi.create()
    val config = PagedList.Config.Builder()
      .setPageSize(10)
      .setInitialLoadSizeHint(20)
      .build()
    val pagedList = PagedList.Builder<Int, Photo>(PhotoDataSource(api), config)
      .setNotifyExecutor(UiThreadExecutor())
      .setFetchExecutor(BackgroundThreadExecutor())
      .build()
    photoList.postValue(pagedList)
  }

  class UiThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
      mHandler.post(command)
    }
  }

  class BackgroundThreadExecutor : Executor {
    private val executorService = Executors.newFixedThreadPool(3)

    override fun execute(command: Runnable) {
      executorService.execute(command)
    }
  }
}