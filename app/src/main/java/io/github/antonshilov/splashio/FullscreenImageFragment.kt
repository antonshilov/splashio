package io.github.antonshilov.splashio


import android.R.attr.uiOptions
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.view.WindowInsetsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.splashio.api.Photo
import kotlinx.android.synthetic.main.fragment_fullscreen_image.*


private const val ARG_PHOTO = "photo"


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
    activity.statusBarHeight.observe(this, Observer<WindowInsetsCompat> { insets ->
      //      TODO: handle insets in horizontal orientation
      val lpToolbar = toolbar
        .layoutParams as ViewGroup.MarginLayoutParams
      lpToolbar.topMargin = insets!!.systemWindowInsetTop
      toolbar.layoutParams = lpToolbar
      val lpBottom = bottomContainer
        .layoutParams as ViewGroup.MarginLayoutParams
      lpBottom.bottomMargin = insets.systemWindowInsetBottom
      bottomContainer.layoutParams = lpBottom

    })
    toolbar.setNavigationOnClickListener {
      findNavController().navigateUp()
    }
    photoView.setOnPhotoTapListener { _, _, _ ->
      fullScreen()
    }

  }


  private fun fullScreen() {

    toolbar.switchVisibility()
    bottomContainer.switchVisibility()

    // BEGIN_INCLUDE (get_current_ui_flags)
    // The UI options currently enabled are represented by a bitfield.
    // getSystemUiVisibility() gives us that bitfield.
    val uiOptions = activity.window.decorView.systemUiVisibility
    var newUiOptions = uiOptions
    // END_INCLUDE (get_current_ui_flags)
    // BEGIN_INCLUDE (toggle_ui_flags)

    // Navigation bar hiding:  Backwards compatible to ICS.
    newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


    // Status bar hiding: Backwards compatible to Jellybean
    newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN

    // Immersive mode: Backward compatible to KitKat.
    // Note that this flag doesn't do anything by itself, it only augments the behavior
    // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
    // all three flags are being toggled together.
    // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
    // Sticky immersive mode differs in that it makes the navigation and status bars
    // semi-transparent, and the UI flag does not get cleared when the user interacts with
    // the screen.
    newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    activity.window.decorView.systemUiVisibility = newUiOptions
    //END_INCLUDE (set_ui_flags)
  }

  fun ViewGroup.switchVisibility() {
    TransitionManager.beginDelayedTransition(this)
    this.visibility = if (this.isVisible) View.INVISIBLE else View.VISIBLE
  }

  private fun isImmersiveModeEnabled(): Boolean {
    return uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
  }

  override fun onStop() {
    super.onStop()
    activity.setSupportActionBar(null)
  }

  override fun onDestroy() {
    super.onDestroy()
    if (isImmersiveModeEnabled()) fullScreen()
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
