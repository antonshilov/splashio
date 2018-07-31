package io.github.antonshilov.domain

import io.github.antonshilov.domain.executor.PostExecutionThread
import io.github.antonshilov.domain.model.Photo
import io.reactivex.Observable

open class GetPhotos constructor(private val repo: PhotoRepo, postExecutionThread: PostExecutionThread) : ObservableUseCase<List<Photo>, Nothing?>(postExecutionThread) {
  public override fun buildObservable(params: Nothing?): Observable<List<Photo>> = repo.getLatestPhotos()
}
