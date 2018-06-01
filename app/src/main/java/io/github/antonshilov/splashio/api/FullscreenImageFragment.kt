package io.github.antonshilov.splashio.api


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf

import io.github.antonshilov.splashio.R


private const val ARG_PHOTO = "photo"

/**
 * A simple [Fragment] subclass.
 * Use the [FullscreenImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FullscreenImageFragment : Fragment() {
  private var photo: Photo? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      photo = it.getParcelable(ARG_PHOTO)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_fullscreen_image, container, false)
  }


  companion object {
    @JvmStatic
    fun bundleArgs(photo: Photo) = bundleOf(ARG_PHOTO to photo)
  }
}
