package io.github.antonshilov.domain.feed.photos

import io.github.antonshilov.domain.PhotoRepo
import io.github.antonshilov.domain.SingleUseCase
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.reactivex.Single

open class GetPhotos constructor(private val repo: PhotoRepo, postExecutionThread: PostExecutionThread) :
  SingleUseCase<List<Photo>, PaginationParams>(postExecutionThread) {
  public override fun buildObservable(params: PaginationParams): Single<List<Photo>> {
    return repo.getLatestPhotos(params.page, params.pageSize)
  }
}
