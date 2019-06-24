package io.github.antonshilov.splashio.ui.featured.pagination

import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.github.antonshilov.domain.feed.PaginationParams
import io.github.antonshilov.domain.feed.photos.GetPhotos
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.ui.featured.pagination.Action.ErrorLoadingPageAction
import io.github.antonshilov.splashio.ui.featured.pagination.Action.LoadFirstPageAction
import io.github.antonshilov.splashio.ui.featured.pagination.Action.LoadNextPageAction
import io.github.antonshilov.splashio.ui.featured.pagination.Action.PageLoadedAction
import io.github.antonshilov.splashio.ui.featured.pagination.Action.StartLoadingNextPage
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine.State.ErrorLoadingFirstPageState
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine.State.LoadingFirstPageState
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine.State.ShowContentState
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

sealed class Action {
  object LoadNextPageAction : Action()
  object LoadFirstPageAction : Action()
  data class ErrorLoadingPageAction(val error: Throwable, val page: Int) : Action()
  data class PageLoadedAction(val itemsLoaded: List<Photo>, val page: Int) : Action()
  data class StartLoadingNextPage(val page: Int) : Action()
}

class PaginationStateMachine(private val getPhotos: GetPhotos) {
  private interface ContainsPhotos {
    val photos: List<Photo>
    val page: Int
  }

  sealed class State {
    object LoadingFirstPageState : State()
    data class ErrorLoadingFirstPageState(val errorMessage: String) : State()
    data class ShowContentState(override val photos: List<Photo>, override val page: Int) : State(), ContainsPhotos
    data class ShowContentAndLoadNextPageState(override val photos: List<Photo>, override val page: Int) : State(), ContainsPhotos
    data class ShowContentAndLoadNextPageErrorState(override val photos: List<Photo>, override val page: Int, val errorMessage: String) :
      State(), ContainsPhotos
  }

  val input: Relay<Action> = PublishRelay.create()
  val state: Observable<State> = input
    .doOnNext { Timber.d("Input Action $it") }
    .reduxStore(
      initialState = LoadingFirstPageState,
      sideEffects = listOf(
        this::loadFirstPageSideEffect,
        this::loadNextPageSideEffect
      ),
      reducer = ::reducer
    )
    .distinctUntilChanged()
    .doOnNext { Timber.d("RxStore state: $it") }

  private fun nextPage(s: State): Observable<Action> {
    val nextPage = (if (s is ContainsPhotos) s.page else 0) + 1
    return getPhotos.exec(PaginationParams(page = nextPage))
      .subscribeOn(Schedulers.io())
      .toObservable()
      .map<Action> { photos ->
        PageLoadedAction(photos, nextPage)
      }
//      .delay(1, TimeUnit.SECONDS)
      .onErrorReturn { error -> ErrorLoadingPageAction(error, nextPage) }
      .startWith(StartLoadingNextPage(nextPage))
  }

  private fun loadFirstPageSideEffect(
    actions: Observable<Action>,
    state: StateAccessor<State>
  ): Observable<Action> =
    actions.ofType(LoadFirstPageAction::class.java)
      .filter { state() !is ContainsPhotos }
      .switchMap {
        nextPage(state())
      }

  private fun loadNextPageSideEffect(
    actions: Observable<Action>,
    state: StateAccessor<State>
  ): Observable<Action> =
    actions
      .ofType(LoadNextPageAction::class.java)
      .switchMap {
        nextPage(state())
      }

  private fun reducer(state: State, action: Action): State {
    return when (action) {
      LoadNextPageAction, LoadFirstPageAction -> state

      is ErrorLoadingPageAction -> if (action.page == 1) {
        ErrorLoadingFirstPageState(action.error.localizedMessage)
      } else state

      is PageLoadedAction -> {
        val items = if (state is ContainsPhotos) {
          state.photos + action.itemsLoaded
        } else action.itemsLoaded
        ShowContentState(items, action.page)
      }

      is StartLoadingNextPage -> {
        if (state is ContainsPhotos && state.page >= 1)
          State.ShowContentAndLoadNextPageState(state.photos, state.page)
        else LoadingFirstPageState
      }
    }
  }
}
