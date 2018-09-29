package io.github.antonshilov.domain.feed.collections

import io.github.antonshilov.domain.ObservableUseCase
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.reactivex.Observable

class GetCollections(private val repo: CollectionsRepo, postExecutionThread: PostExecutionThread) :
  ObservableUseCase<List<Collection>, PaginationParams>(postExecutionThread) {
  public override fun buildObservable(params: PaginationParams): Observable<List<Collection>> {
    return repo.getCollections(params.page, params.pageSize)
  }
}
