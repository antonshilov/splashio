package io.github.antonshilov.domain

import io.github.antonshilov.domain.executor.PostExecutionThread
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T, in Params> constructor(private val postExecutionThread: PostExecutionThread) {

  protected abstract fun buildObservable(params: Params): Observable<T>

  open fun exec(params: Params): Observable<T> {
    return buildObservable(params)
      .subscribeOn(Schedulers.io())
      .observeOn(postExecutionThread.scheduler)
  }
}
