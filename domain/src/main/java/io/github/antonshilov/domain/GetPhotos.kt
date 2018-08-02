package io.github.antonshilov.domain

import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.model.Photo
import io.reactivex.Observable

open class GetPhotos constructor(private val repo: PhotoRepo, postExecutionThread: PostExecutionThread) :
  ObservableUseCase<List<Photo>, GetPhotos.Params?>(postExecutionThread) {
  public override fun buildObservable(params: Params?): Observable<List<Photo>> {
    if (params == null) throw IllegalArgumentException("Params cannot be null")
    return repo.getLatestPhotos(params.page, params.pageSize)
  }

  data class Params(val page: Int = 1, val pageSize: Int = 50)
}
