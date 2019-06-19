package io.github.antonshilov.splashio.ui.featured

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.api.model.Photo
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * [PhotoListViewModel] provides a paginated image list to display the list of curated photos
 */
class PhotoListViewModel(val api: UnsplashApi) : ViewModel() {

  private val factory = PhotoDataSourceFactory(api)
  val networkState = switchMap(factory.sourceLiveData) { it.networkState }

  val photoList: LiveData<PagedList<Photo>>

  init {
    val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .build()
    photoList = LivePagedListBuilder<Int, Photo>(factory, config)
        .setFetchExecutor(BackgroundThreadExecutor())
        .build()
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
