package io.github.antonshilov.domain.feed.photos

import io.github.antonshilov.domain.ObservableUseCase
import io.github.antonshilov.domain.PhotoRepo
import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.reactivex.Observable

open class GetPhotos constructor(private val repo: PhotoRepo, postExecutionThread: PostExecutionThread) :
  ObservableUseCase<List<Photo>, PaginationParams>(postExecutionThread) {
  public override fun buildObservable(params: PaginationParams): Observable<List<Photo>> {
    return repo.getLatestPhotos(params.page, params.pageSize)
  }
}
