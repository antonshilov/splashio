package io.github.antonshilov.splashio

import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.github.antonshilov.splashio.api.AuthInterceptor
import io.github.antonshilov.splashio.api.Photo
import io.github.antonshilov.splashio.api.UnsplashService
import kotlinx.android.synthetic.main.fragment_image_list.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
//    loadImages()
    showImages()
  }

  private fun loadImages() {
//    vm.photos.observe(this, Observer { showImages(it!!) })
//    vm.loadPhotos()
  }

  private fun showImages() {
    val httpClient = OkHttpClient.Builder()
      .addInterceptor(AuthInterceptor())
      .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
      .build()
    val unsplashApi = Retrofit.Builder()
      .client(httpClient)
      .baseUrl(UnsplashService.ENDPOINT)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(UnsplashService::class.java)
    val config = PagedList.Config.Builder()
      .setPageSize(10)
      .setInitialLoadSizeHint(10)
      .build()
    val pagedList = PagedList.Builder<Int, Photo>(PhotoDataSource(unsplashApi), config)
      .setNotifyExecutor(UiThreadExecutor())
      .setFetchExecutor(BackgroundThreadExecutor())
      .build()
    adapter.submitList(pagedList)
  }

  internal inner class UiThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
      mHandler.post(command)
    }
  }

  internal inner class BackgroundThreadExecutor : Executor {
    private val executorService = Executors.newFixedThreadPool(3)

    override fun execute(command: Runnable) {
      executorService.execute(command)
    }
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
