package io.github.antonshilov.splashio

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.github.antonshilov.splashio.api.Photo
import kotlinx.android.synthetic.main.fragment_image_list.*
import timber.log.Timber

class ImageListFragment : Fragment() {
  private lateinit var vm: PhotoListViewModel

  private var listener: OnFragmentInteractionListener? = null
  private lateinit var navigationController: NavController
  private lateinit var adapter: PhotoAdapter


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_image_list, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    vm = ViewModelProviders.of(this).get(PhotoListViewModel::class.java)
    adapter = PhotoAdapter(this.context!!)
    adapter.onItemClickListener = { navigateToFullscreen(it) }
    imageGrid.adapter = adapter
    navigationController = findNavController()
  }

  override fun onStart() {
    super.onStart()
    loadImages()
  }

  private fun loadImages() {
    vm.photos.observe(this, Observer { showImages(it!!) })
    vm.loadPhotos()
  }

  private fun showImages(images: List<Photo>) {
    Timber.d(images.toString())
    adapter.submitList(images)
  }

  private fun navigateToFullscreen(img: Photo) {
    navigationController.navigate(R.id.action_imageListFragment_to_fullscreenImageFragment,
        FullscreenImageFragment.bundleArgs(img))
  }

  // TODO: Rename method, update argument and hook method into UI event
  fun onButtonPressed(uri: Uri) {
    listener?.onFragmentInteraction(uri)
  }

  override fun onDetach() {
    super.onDetach()
    listener = null
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   *
   *
   * See the Android Training lesson [Communicating with Other Fragments]
   * (http://developer.android.com/training/basics/fragments/communicating.html)
   * for more information.
   */
  interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    fun onFragmentInteraction(uri: Uri)
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImageListFragment.
     */
    @JvmStatic
    fun newInstance() = ImageListFragment()
  }
}
