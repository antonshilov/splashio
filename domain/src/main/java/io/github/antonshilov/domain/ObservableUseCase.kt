package io.github.antonshilov.domain

import io.github.antonshilov.domain.executor.PostExecutionThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T, in Params> constructor(private val postExecutionThread: PostExecutionThread) {
  private val disposables = CompositeDisposable()

  protected abstract fun buildObservable(params: Params): Observable<T>

  open fun exec(observer: DisposableObserver<T>, params: Params) {
    val observable = buildObservable(params)
      .subscribeOn(Schedulers.io())
      .observeOn(postExecutionThread.scheduler)
    addDisposable(observable.subscribeWith(observer))
  }

  /**
   * Add [Disposable] to the [CompositeDisposable] to be disposed later
   */
  fun addDisposable(disposable: Disposable) {
    disposables += disposable
  }

  /**
   * Dispose the [disposables]
   */
  fun dispose() {
    with(disposables) {
      if (!isDisposed) dispose()
    }
  }
}
