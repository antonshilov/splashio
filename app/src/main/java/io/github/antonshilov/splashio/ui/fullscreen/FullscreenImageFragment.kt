package io.github.antonshilov.splashio.ui.fullscreen

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.transition.TransitionManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.GlideRequest
import io.github.antonshilov.splashio.R
import kotlinx.android.synthetic.main.fragment_fullscreen_image.*
import timber.log.Timber

private const val ARG_PHOTO = "photoView"

/**
 * Displays the image details in a full screen.
 * Share image url
 * Pinch to zoom gestures
 * Hide/display controls on image tap
 */

class FullscreenImageFragment : Fragment() {
  private lateinit var photo: Photo
  private lateinit var progressIndicator: CircularProgressDrawable

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_fullscreen_image, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    parseArguments()
    setupEnterTransition()
    initProgressIndicator()
    progress.setImageDrawable(progressIndicator)
  }

  private fun parseArguments() {
    photo = arguments?.getSerializable(ARG_PHOTO) as Photo?
      ?: throw IllegalArgumentException("You have to pass a photoView to view in fullscreen")
  }

  private fun setupEnterTransition() {
    // setting up a transition name dynamically because we have a lot of images with the same layout
    // on the previous screen ImageListFragment
    postponeEnterTransition()
    photoView.transitionName = photo.id
  }

  /**
   * Initializes [progressIndicator] property with large size and white color.
   */
  private fun initProgressIndicator() {
    progressIndicator = CircularProgressDrawable(requireContext()).apply {
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
    toolbar.title = null
  }

  private fun setInsetListener() {
    ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
      val lpToolbar = toolbar.layoutParams as ViewGroup.MarginLayoutParams
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
      findNavController().popBackStack()
    }
    photoView.setOnPhotoTapListener { _, _, _ ->
      fullScreen(isImmersiveModeEnabled())
    }
  }

  private fun bindPhoto() {
    loadPhoto()
    userName.text = photo.user.name
    loadAvatar()
  }

  private fun loadAvatar() {
    GlideApp.with(this)
      .load(photo.user.profileImage?.medium)
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
          return false
        }
      })
      .into(photoView)
  }

  private fun getThumbnailLoadRequest(): GlideRequest<Drawable> {
    return GlideApp.with(this)
      .load(photo.urls.regular)
      .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          startPostponedEnterTransition()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          startPostponedEnterTransition()
          return false
        }
      })
  }

//  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//    menuInflater.inflate(R.menu.menu_fullscreen_image, menu)
//    return true
//  }

//  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//    return when (item?.itemId) {
//      R.id.action_share -> {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        shareIntent.putExtra(Intent.EXTRA_TEXT, photo.links.html)
//        startActivity(Intent.createChooser(shareIntent, "Share photoView using"))
//        true
//      }
//      else -> false
//    }
//  }

  /**
   * Hide/show all UI components except the image to give the user some space :)
   */
  private fun fullScreen(isEnabled: Boolean) {
    Timber.d("Fullscreen toggle: $isEnabled")
    TransitionManager.beginDelayedTransition(root)
    toolbar.isVisible = isEnabled
    bottomContainer.isVisible = isEnabled
  }

  private fun isImmersiveModeEnabled(): Boolean = !toolbar.isVisible

  override fun onStop() {
    super.onStop()
    if (isImmersiveModeEnabled()) fullScreen(false)
  }

  companion object {
    @JvmStatic
    fun bundleArgs(photo: Photo) = bundleOf(ARG_PHOTO to photo)
  }
}
