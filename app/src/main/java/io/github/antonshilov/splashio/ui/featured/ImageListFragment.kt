package io.github.antonshilov.splashio.ui.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.R
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

  private var adapter: PhotoAdapter = PhotoAdapter()
  private lateinit var imageGrid: RecyclerView
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
    adapter.onItemClickListener = { photo, sharedView -> navigateToFullscreen(photo, sharedView) }
    vm.photoList.observe(this, Observer {
      Timber.d("photoView list has been set to the adapter")
      adapter.submitList(it)
    })
    imageGrid.adapter = adapter
  }

  /**
   * Changes the view visibility based on the network state
   */
  private fun showNetworkState(networkState: NetworkState?) {
    progressBar.isVisible = networkState?.status == Status.RUNNING
    progressBar.isVisible = networkState?.status == Status.SUCCESS
    progressBar.isVisible = networkState?.status == Status.FAILED
  }

  private fun navigateToFullscreen(img: Photo, sharedView: ImageView) {
    val extras = FragmentNavigatorExtras(sharedView to img.id)
    findNavController().navigate(
      R.id.fullscreenImageFragment,
      FullscreenImageFragment.bundleArgs(img), // Bundle of args
      null, // NavOptions
      extras
    )
  }
}
