package io.github.antonshilov.splashio

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.github.antonshilov.splashio.api.model.Photo
import timber.log.Timber

class ImageListFragment : Fragment() {
  private lateinit var vm: PhotoListViewModel

  private lateinit var navigationController: NavController
  private lateinit var adapter: PhotoAdapter


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_image_list, container, false)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    vm = ViewModelProviders.of(this).get(PhotoListViewModel::class.java)
    navigationController = findNavController()
    adapter = PhotoAdapter(this.context!!)
    adapter.onItemClickListener = { navigateToFullscreen(it) }
    vm.photoList.observe(this, Observer {
      Timber.e("SUBMITLIST")
      adapter.submitList(it)
    })
    vm.loadPhotos()
  }

  override fun onStart() {
    super.onStart()
    imageGrid.adapter = adapter
  }


  private fun navigateToFullscreen(img: Photo) {
    navigationController.navigate(R.id.action_imageListFragment_to_fullscreenImageFragment,
      FullscreenImageFragment.bundleArgs(img))
  }

}
