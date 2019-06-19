package io.github.antonshilov.splashio

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class RxViewModel : ViewModel() {
  protected val disposables = CompositeDisposable()
  override fun onCleared() {
    disposables.clear()
    super.onCleared()
  }
}
