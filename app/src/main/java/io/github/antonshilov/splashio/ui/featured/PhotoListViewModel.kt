package io.github.antonshilov.splashio.ui.featured

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.os.Handler
import android.os.Looper
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.api.model.Photo
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * [PhotoListViewModel] provides a paginated image list to display the list of curated photos
 */
class PhotoListViewModel(val api: UnsplashApi) : ViewModel() {
  val photoList = MutableLiveData<PagedList<Photo>>()
  private val pagedList: PagedList<Photo>

  init {
    val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .build()
    pagedList = PagedList.Builder<Int, Photo>(PhotoDataSource(api), config)
        .setNotifyExecutor(UiThreadExecutor())
        .setFetchExecutor(BackgroundThreadExecutor())
        .build()
    photoList.postValue(pagedList)
    Timber.d("View Model has been initialized")
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