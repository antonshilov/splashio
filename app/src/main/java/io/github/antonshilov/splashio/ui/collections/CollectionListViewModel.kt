package io.github.antonshilov.splashio.ui.collections

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.domain.feed.collections.GetCollections
import io.reactivex.disposables.Disposable
import timber.log.Timber

class CollectionListViewModel(private val getCollections: GetCollections) : ViewModel() {

  private var disposable: Disposable? = null

  val collections = MutableLiveData<List<Collection>>()

  fun fetchCollections() {
    disposable = getCollections.exec(PaginationParams())
      .subscribe({
        collections.postValue(it)
      }, {
        Timber.e(it)
      })
  }

  override fun onCleared() {
    disposable?.dispose()
    super.onCleared()
  }
}
