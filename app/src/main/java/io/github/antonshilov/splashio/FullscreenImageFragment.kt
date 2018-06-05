package io.github.antonshilov.splashio


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.splashio.api.Photo
import kotlinx.android.synthetic.main.fragment_fullscreen_image.*


private const val ARG_PHOTO = "photo"

/**
 * A simple [Fragment] subclass.
 * Use the [FullscreenImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FullscreenImageFragment : Fragment() {
  private var photo: Photo? = null
  private lateinit var activity: MainActivity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      photo = it.getParcelable(ARG_PHOTO)
    }
  }

  override fun onStart() {
    super.onStart()
    photo?.let {
      Glide.with(this)
        .load(it.urls.full)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(photoView)
    }
    activity = this.getActivity() as MainActivity
    activity.statusBarHeight.observe(this, Observer<Int> { height ->
      val lpToolbar = toolbar
        .layoutParams as ViewGroup.MarginLayoutParams
      lpToolbar.topMargin += height!!
      toolbar.layoutParams = lpToolbar
    })
    toolbar.setNavigationOnClickListener {
      findNavController().navigateUp()
    }

  }


  override fun onStop() {
    super.onStop()
    activity.setSupportActionBar(null)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_fullscreen_image, container, false)
  }


  companion object {
    @JvmStatic
    fun bundleArgs(photo: Photo) = bundleOf(ARG_PHOTO to photo)
  }
}
