package io.github.antonshilov.splashio.ui.featured

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import io.github.antonshilov.domain.PhotoRepo
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

enum class Status {
  RUNNING,
  SUCCESS,
  FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
  val status: Status,
  val msg: String? = null
) {
  companion object {
    val LOADED = NetworkState(Status.SUCCESS)
    val LOADING = NetworkState(Status.RUNNING)
    fun error(msg: String?) = NetworkState(Status.FAILED, msg)
  }
}

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 */
class PhotoDataSourceFactory(
  private val api: PhotoRepo,
  private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Photo>() {
  val sourceLiveData = MutableLiveData<PhotoDataSource>()
  override fun create(): DataSource<Int, Photo> {
    val source = PhotoDataSource(api, compositeDisposable)
    sourceLiveData.postValue(source)
    return source
  }
}

class PhotoDataSource(private val api: PhotoRepo, private val compositeDisposable: CompositeDisposable) :
  PageKeyedDataSource<Int, Photo>() {

  val networkState = MutableLiveData<NetworkState>()

  override fun loadInitial(
    params: LoadInitialParams<Int>,
    callback: LoadInitialCallback<Int,
      Photo>
  ) {
    networkState.postValue(NetworkState.LOADING)
    api.getLatestPhotos(pageSize = params.requestedLoadSize, page = 1)
      .subscribeBy(
        onSuccess = {
          callback.onResult(it, 1, 2)
          networkState.postValue(NetworkState.LOADED)
          Timber.d("Initial load of curated photos succeed")
        },
        onError = {
          networkState.postValue(NetworkState.error(it.message ?: "unknown error"))
          Timber.e(it, "Initial load of curated photos failed")
        }
      ).addTo(compositeDisposable)
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    api.getLatestPhotos(pageSize = params.requestedLoadSize, page = params.key)
      .subscribeBy(
        onSuccess = {
          Timber.d("Load of curated photos succeed")
          callback.onResult(it, params.key + 1)
        },
        onError = {
          Timber.e(it, "Load of curated photos has failed")
        }
      ).addTo(compositeDisposable)
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    //    No need to load before
  }
}
