package io.github.antonshilov.splashio.ui.featured

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.featured.pagination.PaginationStateMachine
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageFragment
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeaturedPhotosViewBinding(protected val root: ViewGroup, protected val navigator: NavController) {
  protected val imageGrid: EpoxyRecyclerView = root.findViewById(R.id.imageGrid)
  protected val progressBar: ProgressBar = root.findViewById(R.id.progress)
  protected val errorText: TextView = root.findViewById(R.id.errorText)
  protected val controller = PhotoController(this::navigateToFullscreen)

  val endOfListReached = Observable.create<Unit> { emitter ->
    val listener = object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val endReached = !recyclerView.canScrollVertically(1)
        Timber.d("Scroll changed: $endReached")
        if (endReached) {
          emitter.onNext(Unit)
        }
      }
    }

    emitter.setCancellable { imageGrid.removeOnScrollListener(listener) }
    imageGrid.addOnScrollListener(listener)
  }.debounce(200, TimeUnit.MILLISECONDS)

  init {
    imageGrid.setController(controller)
  }

  fun render(state: PaginationStateMachine.State) {
    when (state) {
      PaginationStateMachine.State.LoadingFirstPageState -> {
        imageGrid.gone()
        progressBar.visible()
        errorText.gone()
      }
      is PaginationStateMachine.State.ErrorLoadingFirstPageState -> {
        imageGrid.gone()
        progressBar.gone()
        errorText.text = state.errorMessage
        errorText.visible()
      }
      is PaginationStateMachine.State.ShowContentState -> showPhotos(state.photos, false)
      is PaginationStateMachine.State.ShowContentAndLoadNextPageState -> showPhotos(state.photos, true)
      is PaginationStateMachine.State.ShowContentAndLoadNextPageErrorState -> TODO()
    }
  }

  private fun showPhotos(photos: List<Photo>, loadingNextPage: Boolean) {
    progressBar.gone()
    errorText.gone()
    imageGrid.visible()
    controller.setItems(photos, loadingNextPage)
  }

  private fun navigateToFullscreen(photo: Photo, sharedView: ImageView) {
    val extras = FragmentNavigatorExtras(sharedView to photo.id)
    navigator.navigate(
      R.id.fullscreenImageFragment,
      FullscreenImageFragment.bundleArgs(photo), // Bundle of args
      null, // NavOptions
      extras
    )
  }

  private fun View.gone() {
    visibility = View.GONE
  }

  private fun View.visible() {
    visibility = View.VISIBLE
  }
}
