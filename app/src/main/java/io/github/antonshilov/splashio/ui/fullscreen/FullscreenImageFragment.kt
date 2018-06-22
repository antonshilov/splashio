package io.github.antonshilov.splashio.ui.fullscreen


import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.view.WindowInsetsCompat
import android.support.v4.widget.CircularProgressDrawable
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.model.Photo
import io.github.antonshilov.splashio.ui.MainActivity
import io.github.antonshilov.splashio.ui.featured.setVisibility
import kotlinx.android.synthetic.main.fragment_fullscreen_image.*
import org.koin.android.architecture.ext.viewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber


private const val ARG_PHOTO = "photo"

/**
 * Displays the image details in a full screen.
 * Share image url
 * Pinch to zoom gestures
 * Hide/display controls on image tap
 */
@RuntimePermissions
class FullscreenImageFragment : Fragment() {
  private val vm by viewModel<FullscreenImageViewModel>()
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
      photoView.isEnabled = false
      val thumbnailRequest = GlideApp.with(this)
          .load(photo.url)

      GlideApp.with(this)
          .load(it.urls.full)
          .thumbnail(thumbnailRequest)
          .transition(DrawableTransitionOptions.withCrossFade())
          .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
              photoView.isEnabled = true
              return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
              photoView.isEnabled = true
              return false
            }

          })
          .into(photoView)
    }
    userName.text = photo.user.name
    GlideApp.with(this)
        .load(photo.user.profileImage.medium)
        .circleCrop()
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
      fullScreen(isImmersiveModeEnabled())
    }
    buttonWallpaper.setOnClickListener {
      setWallpaperWithPermissionCheck()
    }
    activity.setSupportActionBar(toolbar)
    toolbar.title = null

  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    onRequestPermissionsResult(requestCode, grantResults)
  }

  /**
   * Call view model to set a wallpaper
   * annotated to generated checked permissions method
   */
  @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
  fun setWallpaper() {
    vm.setWallpaper(photo)
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

  /**
   * Hide/show all UI components except the image to give the user some space :)
   */
  private fun fullScreen(isEnabled: Boolean) {
    Timber.d("Fullscreen toggle: $isEnabled")
    TransitionManager.beginDelayedTransition(frameLayout)
    toolbar.setVisibility(isEnabled)
    bottomContainer.setVisibility(isEnabled)
  }

  private fun isImmersiveModeEnabled(): Boolean = !toolbar.isVisible

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
