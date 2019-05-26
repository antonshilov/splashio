package io.github.antonshilov.splashio.ui.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.domain.feed.collections.GetCollections
import io.reactivex.disposables.CompositeDisposable

class CollectionListViewModel(getCollections: GetCollections) : ViewModel() {

  private val compositeDisposable = CompositeDisposable()

  val collectionsList: LiveData<PagedList<Collection>>

  private val PAGE_SIZE = 50

  init {
    collectionsList =
      LivePagedListBuilder(CollectionDataSourceFactory(getCollections, compositeDisposable), PAGE_SIZE).build()
  }

  override fun onCleared() {
    compositeDisposable.dispose()
    super.onCleared()
  }
}

class CollectionDataSource(private val getCollections: GetCollections, val compositeDisposable: CompositeDisposable) :
  PageKeyedDataSource<Int, Collection>() {
  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Collection>) {
    val paginationParams = PaginationParams(pageSize = params.requestedLoadSize)
    loadPage(paginationParams) { callback.onResult(it, paginationParams.page, paginationParams.nextPage) }
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Collection>) {
    val paginationParams = PaginationParams(params.key, params.requestedLoadSize)
    loadPage(paginationParams) { callback.onResult(it, paginationParams.nextPage) }
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Collection>) {
    // this method is not needed for us
  }

  private fun loadPage(paginationParams: PaginationParams, callback: (List<Collection>) -> Unit) {
    val disposable = getCollections.exec(paginationParams)
      .subscribe { callback.invoke(it) }
    compositeDisposable.add(disposable)
  }
}

class CollectionDataSourceFactory(
  private val getCollections: GetCollections,
  val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Collection>() {
  override fun create(): DataSource<Int, Collection> {
    return CollectionDataSource(getCollections, compositeDisposable)
  }
}
