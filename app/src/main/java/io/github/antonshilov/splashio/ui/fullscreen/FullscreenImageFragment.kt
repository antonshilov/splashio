package io.github.antonshilov.splashio.ui.fullscreen

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.DetailsTransition
import io.github.antonshilov.splashio.ui.doOnApplyWindowInsets
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
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sharedElementEnterTransition = DetailsTransition()
    sharedElementReturnTransition = DetailsTransition()
    enterTransition = Fade()
    exitTransition = Fade()
    parseArguments()
    initProgressIndicator()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_fullscreen_image, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpToolbar()
    setupEnterTransition()
    progress.setImageDrawable(progressIndicator)
    photoView.isEnabled = false
    bindPhoto()
    setInsetListener()
    setUpClickListeners()
  }

  private fun setUpToolbar() {
    toolbar.isVisible = true
    toolbar.inflateMenu(R.menu.menu_fullscreen_image)
    toolbar.setOnMenuItemClickListener { item ->
      when (item.itemId) {
        R.id.action_share -> {
          val shareIntent = Intent(Intent.ACTION_SEND)
          shareIntent.type = "text/plain"
          shareIntent.putExtra(Intent.EXTRA_TEXT, photo.links.html)
          startActivity(Intent.createChooser(shareIntent, "Share photoView using"))
          true
        }
        else -> false
      }
    }
  }

  private fun parseArguments() {
    photo = arguments?.getSerializable(ARG_PHOTO) as Photo?
      ?: throw IllegalArgumentException("You have to pass a photoView to view in fullscreen")
  }

  private fun setupEnterTransition() {
    // setting up a transition name dynamically because we have a lot of images with the same layout
    // on the previous screen ImageListFragment
    photoView.transitionName = photo.id
    postponeEnterTransition()
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

  private fun setInsetListener() {
    toolbar.doOnApplyWindowInsets { insetView, insets, padding ->
      insetView.updatePadding(
        top = padding.top + insets.systemWindowInsetTop,
        left = padding.left + insets.systemWindowInsetLeft,
        right = padding.right + insets.systemWindowInsetRight
      )
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
      .load(photo.urls.regular)
      .listener(object : RequestListener<Drawable?> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
          startPostponedEnterTransition()
          photoView.isEnabled = true
          progressIndicator.stop()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable?>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          startPostponedEnterTransition()
          photoView.isEnabled = true
          progressIndicator.stop()
          return false
        }
      })
      .into(photoView)
  }

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

  companion object {
    @JvmStatic
    fun bundleArgs(photo: Photo) = bundleOf(ARG_PHOTO to photo)
  }
}
