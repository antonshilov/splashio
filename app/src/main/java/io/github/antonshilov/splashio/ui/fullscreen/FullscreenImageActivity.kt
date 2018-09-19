package io.github.antonshilov.splashio.ui.fullscreen

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.view.ViewCompat
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.GlideRequest
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.model.Photo
import io.github.antonshilov.splashio.ui.featured.setVisibility
import kotlinx.android.synthetic.main.activity_fullscreen_image.*
import org.koin.android.architecture.ext.android.viewModel
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
class FullscreenImageActivity : AppCompatActivity() {
  private val vm by viewModel<FullscreenImageViewModel>()
  private lateinit var photo: Photo
  private lateinit var progressIndicator: CircularProgressDrawable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fullscreen_image)

    photo = intent?.extras?.getParcelable(ARG_PHOTO) ?:
      throw IllegalArgumentException("You have to pass a photo to view in fullscreen")
    setupEnterTransition()
    initProgressIndicator()
    progress.setImageDrawable(progressIndicator)
  }

  private fun setupEnterTransition() {
    supportPostponeEnterTransition()
    // setting up a transition name dynamically because we have a lot of images with the same layout
    // on the previous screen ImageListFragment
    photoView.transitionName = photo.id
  }

  /**
   * Initializes [progressIndicator] property with large size and white color.
   */
  private fun initProgressIndicator() {
    progressIndicator = CircularProgressDrawable(this).apply {
      setStyle(CircularProgressDrawable.DEFAULT)
      setColorSchemeColors(Color.WHITE)
      start()
    }
  }

  override fun onStart() {
    super.onStart()

    photoView.isEnabled = false
    bindPhoto()
    setInsetListener()
    setUpClickListeners()
    this.setSupportActionBar(toolbar)
    toolbar.title = null
  }

  private fun setInsetListener() {
    ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
      val lpToolbar = toolbar
        .layoutParams as ViewGroup.MarginLayoutParams
      lpToolbar.topMargin = insets!!.systemWindowInsetTop
      toolbar.layoutParams = lpToolbar
      val lpBottom = bottomContainer
        .layoutParams as ViewGroup.MarginLayoutParams
      lpBottom.bottomMargin = insets.systemWindowInsetBottom
      bottomContainer.layoutParams = lpBottom
      insets.consumeSystemWindowInsets()
    }
  }

  private fun setUpClickListeners() {
    toolbar.setNavigationOnClickListener {
      onBackPressed()
    }
    photoView.setOnPhotoTapListener { _, _, _ ->
      fullScreen(isImmersiveModeEnabled())
    }
    buttonWallpaper.setOnClickListener {
      setWallpaperWithPermissionCheck()
    }
  }

  private fun bindPhoto() {
    loadPhoto()
    userName.text = photo.user?.name
    loadAvatar()
  }

  private fun loadAvatar() {
    GlideApp.with(this)
      .load(photo.user?.profileImage?.medium)
      .circleCrop()
      .into(avatar)
  }

  private fun loadPhoto() {
    GlideApp.with(this)
      .load(photo.urls.full)
      .thumbnail(getThumbnailLoadRequest())
      .transition(DrawableTransitionOptions.withCrossFade())
      .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          photoView.isEnabled = true
          progressIndicator.stop()
          supportStartPostponedEnterTransition()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          photoView.isEnabled = true
          progressIndicator.stop()
          supportStartPostponedEnterTransition()
          return false
        }
      })
      .into(photoView)
  }

  private fun getThumbnailLoadRequest(): GlideRequest<Drawable> {
    return GlideApp.with(this)
      .load(photo.url)
      .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          supportStartPostponedEnterTransition()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          supportStartPostponedEnterTransition()
          return false
        }
      })
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

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_fullscreen_image, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.action_share -> {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, photo.links?.html)
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
    TransitionManager.beginDelayedTransition(root)
    toolbar.setVisibility(isEnabled)
    bottomContainer.setVisibility(isEnabled)
  }

  private fun isImmersiveModeEnabled(): Boolean = !toolbar.isVisible

  override fun onDestroy() {
    super.onDestroy()
    if (isImmersiveModeEnabled()) fullScreen(false)
  }

  companion object {
    @JvmStatic
    fun bundleArgs(photo: Photo) = bundleOf(ARG_PHOTO to photo)
  }
}
