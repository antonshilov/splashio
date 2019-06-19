package io.github.antonshilov.domain.feed.collections

import io.github.antonshilov.domain.SingleUseCase
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.reactivex.Single

open class GetCollections(private val repo: CollectionsRepo, postExecutionThread: PostExecutionThread) :
  SingleUseCase<List<Collection>, PaginationParams>(postExecutionThread) {
  public override fun buildObservable(params: PaginationParams): Single<List<Collection>> {
    return repo.getCollections(params.page, params.pageSize)
  }
}
