package io.github.antonshilov.splashio


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.view.WindowInsetsCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.antonshilov.splashio.api.model.Photo
import kotlinx.android.synthetic.main.fragment_fullscreen_image.*


private const val ARG_PHOTO = "photo"


class FullscreenImageFragment : Fragment() {
  private lateinit var photo: Photo
  private lateinit var activity: MainActivity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      photo = it.getParcelable(ARG_PHOTO)
    }
    setHasOptionsMenu(true)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    activity = this.getActivity() as MainActivity

  }

  override fun onStart() {
    super.onStart()
    photo.let {
      Glide.with(this)
        .load(it.urls.full)
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

  fun AppCompatActivity.fullSreen(isEnabled: Boolean) {
    // BEGIN_INCLUDE (get_current_ui_flags)
    // The UI options currently enabled are represented by a bitfield.
    // getSystemUiVisibility() gives us that bitfield.
    if (isEnabled == isImmersiveModeEnabled()) return
    val uiOptions = this.window.decorView.systemUiVisibility
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

    this.window.decorView.systemUiVisibility = newUiOptions
    //END_INCLUDE (set_ui_flags)
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
