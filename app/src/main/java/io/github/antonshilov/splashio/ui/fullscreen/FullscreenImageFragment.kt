package io.github.antonshilov.splashio.ui.fullscreen


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.view.WindowInsetsCompat
import android.support.v4.widget.CircularProgressDrawable
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.model.Photo
import io.github.antonshilov.splashio.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_fullscreen_image.*


private const val ARG_PHOTO = "photo"

/**
 * Displays the image details in a full screen.
 * Share image url
 * Pinch to zoom gestures
 * Hide/display controls on image tap
 */
class FullscreenImageFragment : Fragment() {
  private lateinit var photo: Photo
  private lateinit var activity: MainActivity
  private lateinit var progressIndicator: CircularProgressDrawable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      photo = it.getParcelable(ARG_PHOTO)
    }

    setHasOptionsMenu(true)
    initProgressIndicator()
  }

  /**
   * Initializes [progressIndicator] property with large size and white color.
   */
  private fun initProgressIndicator() {
    progressIndicator = CircularProgressDrawable(context!!).apply {
      setStyle(CircularProgressDrawable.LARGE)
      setColorSchemeColors(Color.WHITE)
      start()
    }
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    activity = this.getActivity() as MainActivity

  }

  override fun onStart() {
    super.onStart()
    photo.let {
      GlideApp.with(this)
          .load(it.urls.full)
          .placeholder(progressIndicator)
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(photoView)
    }
    userName.text = photo.user.name
    GlideApp.with(this)
        .load(photo.user.profileImage.medium)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(avatar)

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
      fullScreen(!isImmersiveModeEnabled())
    }
    activity.setSupportActionBar(toolbar)
    toolbar.title = null

  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_fullscreen_image, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action_share -> {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, photo.links.html)
        startActivity(Intent.createChooser(shareIntent, "Share photo using"))
        true
      }
      else -> false
    }
  }

  private fun fullScreen(isEnabled: Boolean) {
    toolbar.switchVisibility(isEnabled)
    bottomContainer.switchVisibility(isEnabled)
//    activity.fullSreen(isEnabled)
  }

  fun ViewGroup.switchVisibility(isEnabled: Boolean) {
    TransitionManager.beginDelayedTransition(this)
    this.visibility = if (isEnabled) View.INVISIBLE else View.VISIBLE
  }

  private fun isImmersiveModeEnabled(): Boolean {
//    val uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility()
//    return uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
    return !toolbar.isVisible
  }

  override fun onDestroyView() {
    super.onDestroyView()
    if (isImmersiveModeEnabled()) fullScreen(false)
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
