package io.github.antonshilov.splashio.ui.featured

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.model.Photo
import io.github.antonshilov.splashio.ui.fullscreen.FullscreenImageFragment
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber


/**
 * [ImageListFragment]
 *
 * displays a grid of the curated images
 * navigates to the [FullscreenImageFragment] on click on the image item
 */
class ImageListFragment : Fragment() {
  private val vm by viewModel<PhotoListViewModel>()

  private lateinit var navigationController: NavController
  private lateinit var adapter: PhotoAdapter
  private lateinit var imageGrid: RecyclerView
  private lateinit var progressBar: ProgressBar
  private lateinit var errorText: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    navigationController = findNavController()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
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
    adapter.onItemClickListener = { navigateToFullscreen(it) }
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


  private fun navigateToFullscreen(img: Photo) {
    navigationController.navigate(R.id.action_imageListFragment_to_fullscreenImageFragment,
        FullscreenImageFragment.bundleArgs(img))
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