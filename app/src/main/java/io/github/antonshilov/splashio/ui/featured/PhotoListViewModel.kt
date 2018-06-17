package io.github.antonshilov.splashio.ui.featured

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.os.Handler
import android.os.Looper
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.api.model.Photo
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
 */
class PhotoListViewModel(val api: UnsplashApi) : ViewModel() {
  val photoList = MutableLiveData<PagedList<Photo>>()
  val pagedList: PagedList<Photo>

  init {
    val config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(20)
        .build()
    pagedList = PagedList.Builder<Int, Photo>(PhotoDataSource(api), config)
        .setNotifyExecutor(UiThreadExecutor())
        .setFetchExecutor(BackgroundThreadExecutor())
        .build()


  }

  fun loadPhotos() {
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