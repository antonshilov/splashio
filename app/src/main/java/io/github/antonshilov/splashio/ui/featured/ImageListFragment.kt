package io.github.antonshilov.splashio.ui.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.doOnApplyWindowInsets
import io.github.antonshilov.splashio.ui.featured.pagination.Action
import io.github.antonshilov.splashio.ui.featured.pagination.Action.LoadFirstPageAction
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_image_list.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * [ImageListFragment]
 *
 * displays a grid of the curated images
 * navigates to the [FullscreenImageFragment] on click on the image item
 */
class ImageListFragment : Fragment() {

  private val vm by viewModel<PhotoListViewModel>()
  private lateinit var binding: FeaturedPhotosViewBinding
  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    vm.input.accept(LoadFirstPageAction)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_image_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    applyWindowInsets()
    binding = FeaturedPhotosViewBinding(rootView, findNavController())
    vm.state.observe(this, Observer { binding.render(it) })
    binding.endOfListReached
      .map { Action.LoadNextPageAction }
      .subscribe(vm.input)
      .addTo(disposables)
  }

  private fun applyWindowInsets() {
    imageGrid.doOnApplyWindowInsets { insetView, insets, padding ->
      insetView.updatePadding(
        top = padding.top + insets.systemWindowInsetTop,
        bottom = padding.bottom + insets.systemWindowInsetBottom,
        left = padding.left + insets.systemWindowInsetLeft,
        right = padding.right + insets.systemWindowInsetRight
      )
    }
  }

  override fun onDestroy() {
    disposables.clear()
    super.onDestroy()
  }
}
