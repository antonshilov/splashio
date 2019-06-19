package io.github.antonshilov.domain

import io.github.antonshilov.domain.executor.PostExecutionThread
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, in Params> constructor(private val postExecutionThread: PostExecutionThread) {

  protected abstract fun buildObservable(params: Params): Single<T>

  open fun exec(params: Params): Single<T> {
    return buildObservable(params)
      .subscribeOn(Schedulers.io())
      .observeOn(postExecutionThread.scheduler)
  }
}
