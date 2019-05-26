package io.github.antonshilov.splashio.ui.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.model.Photo
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageActivity
import io.github.antonshilov.splashio.ui.navigation.Scrollable
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [ImageListFragment]
 *
 * displays a grid of the curated images
 * navigates to the [FullscreenImageActivity] on click on the image item
 */
class ImageListFragment : Fragment(), Scrollable {

  private val vm by viewModel<PhotoListViewModel>()

  private lateinit var adapter: PhotoAdapter
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
   * Initialize the photo adapter to handle
   * Item clicks
   * PagedList
   * and attach is to the [RecyclerView] to display list of photos
   */
  private fun initAdapter() {
    adapter = PhotoAdapter(this.context!!)
    adapter.onItemClickListener = { photo, sharedView -> navigateToFullscreen(photo, sharedView) }
    vm.photoList.observe(this, Observer {
      Timber.d("photo list has been set to the adapter")
      adapter.submitList(it)
    })
    imageGrid.adapter = adapter
  }

  /**
   * Changes the view visibility based on the network state
   */
  private fun showNetworkState(networkState: NetworkState?) {
    progressBar.setVisibility(networkState?.status == Status.RUNNING)
    imageGrid.setVisibility(networkState?.status == Status.SUCCESS)
    errorText.setVisibility(networkState?.status == Status.FAILED)
  }

  override fun scrollTop() {
    imageGrid.smoothScrollToPosition(0)
  }

  private fun navigateToFullscreen(img: Photo, sharedView: ImageView) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, sharedView, img.id)
    val extras = ActivityNavigatorExtras(options)
    findNavController().navigate(
      R.id.fullscreenImageActivity,
      FullscreenImageActivity.bundleArgs(img), // Bundle of args
      null, // NavOptions
      extras
    )
  }
}

/**
 * Changes view visibility
 * @param isVisible true==VISIBLE false==GONE
 */
fun View.setVisibility(isVisible: Boolean) {
  this.visibility = if (isVisible) {
    View.VISIBLE
  } else {
    View.GONE
  }
}
