package io.github.antonshilov.splashio.ui.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.github.antonshilov.splashio.RxViewModel
import io.github.antonshilov.splashio.ui.featured.pagination.Action
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine.State
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo

/**
 * [PhotoListViewModel] provides a paginated image list to display the list of curated photos
 */
class PhotoListViewModel(paginationStateMachine: PaginationStateMachine, androidScheduler: Scheduler) : RxViewModel() {
  private val inputRelay: Relay<Action> = PublishRelay.create()
  private val mutableState = MutableLiveData<State>()

  val input: Consumer<Action> = inputRelay
  val state: LiveData<State> = mutableState

  init {
    inputRelay.subscribe(paginationStateMachine.input).addTo(disposables)
    paginationStateMachine.state
      .observeOn(androidScheduler)
      .subscribe { state -> mutableState.value = state }
      .addTo(disposables)
  }
}
