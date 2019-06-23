package io.github.antonshilov.splashio.ui.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.doOnApplyWindowInsets
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageFragment
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [ImageListFragment]
 *
 * displays a grid of the curated images
 * navigates to the [FullscreenImageFragment] on click on the image item
 */
class ImageListFragment : Fragment() {

  private val vm by viewModel<PhotoListViewModel>()
  private val controller = PhotoController(this::navigateToFullscreen)
  private lateinit var imageGrid: EpoxyRecyclerView
  private lateinit var progressBar: ProgressBar
  private lateinit var errorText: TextView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_image_list, container, false)
    imageGrid = view.findViewById(R.id.imageGrid)
    progressBar = view.findViewById(R.id.progress)
    errorText = view.findViewById(R.id.errorText)
    initAdapter()
    initNetworkState()
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    imageGrid.doOnApplyWindowInsets { insetView, insets, padding ->
      insetView.updatePadding(
        top = padding.top + insets.systemWindowInsetTop,
        bottom = padding.bottom + insets.systemWindowInsetBottom,
        left = padding.left + insets.systemWindowInsetLeft,
        right = padding.right + insets.systemWindowInsetRight
      )
    }
    ViewCompat.requestApplyInsets(view)
  }

  /**
   * Subscribe to network changed to display them
   */
  private fun initNetworkState() {
    vm.networkState.observe(this, Observer {
      showNetworkState(it)
    })
  }

  /**
   * Initialize the photoView adapter to handle
   * Item clicks
   * PagedList
   * and attach is to the [RecyclerView] to display list of photos
   */
  private fun initAdapter() {
    vm.photoList.observe(this, Observer {
      Timber.d("photoView list has been set to the adapter")
      controller.submitList(it)
    })
    imageGrid.setController(controller)
  }

  /**
   * Changes the view visibility based on the network state
   */
  private fun showNetworkState(networkState: NetworkState?) {
    progressBar.isVisible = networkState?.status == Status.RUNNING
    progressBar.isVisible = networkState?.status == Status.SUCCESS
    progressBar.isVisible = networkState?.status == Status.FAILED
  }

  private fun navigateToFullscreen(photo: Photo, sharedView: ImageView) {
    val extras = FragmentNavigatorExtras(sharedView to photo.id)
    findNavController().navigate(
      R.id.fullscreenImageFragment,
      FullscreenImageFragment.bundleArgs(photo), // Bundle of args
      null, // NavOptions
      extras
    )
  }
}
