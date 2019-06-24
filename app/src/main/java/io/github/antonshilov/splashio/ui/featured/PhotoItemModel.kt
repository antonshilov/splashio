package io.github.antonshilov.splashio.ui.featured

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.ModelView
import com.antonshilov.widgets.AspectRatioImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.epoxy.KotlinEpoxyHolder
import io.github.antonshilov.splashio.ui.featured.PhotoItemModel.Holder

typealias PhotoCardClickListener = ((photo: Photo, sharedImageView: ImageView) -> Unit)

@EpoxyModelClass(layout = R.layout.item_photo)
abstract class PhotoItemModel : EpoxyModelWithHolder<Holder>() {
  @EpoxyAttribute
  lateinit var photo: Photo
  @EpoxyAttribute(DoNotHash)
  var clickListener: View.OnClickListener? = null

  override fun bind(holder: Holder) {
    with(holder) {
      val thumbnailRequest = GlideApp.with(photoView)
        .load(photo.urls.thumb)
        .transition(DrawableTransitionOptions.withCrossFade())
      Glide.with(photoView)
        .load(photo.urls.regular)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(photoView)
      photoView.setAspectRatio(photo.width, photo.height)
      photoView.setOnClickListener(clickListener)
      photoView.background.setTint(Color.parseColor(photo.color))
      photoView.transitionName = photo.id
      photoView.clipToOutline = true
    }
  }

  override fun unbind(holder: Holder) {
    super.unbind(holder)
    holder.photoView.setOnClickListener(null)
    holder.photoView.setImageDrawable(null)
  }

  class Holder : KotlinEpoxyHolder() {
    val photoView by bind<AspectRatioImageView>(R.id.photo)
  }
}

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadingRow @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  init {
    inflate(context, R.layout.loading_row, this)
  }
}

class PhotoController(private val photoCardClickListener: PhotoCardClickListener) : EpoxyController() {

  private var photos: List<Photo> = emptyList()
  private var isLoadingNext = false
  fun setItems(photos: List<Photo>, isLoadingNext: Boolean) {
    this.photos = photos
    this.isLoadingNext = isLoadingNext
  }

  override fun buildModels() {
    photos.forEach {
      photoItem {
        id(it.id)
        photo(it)
        clickListener { model, parentView, _, _ ->
          photoCardClickListener.invoke(model.photo, parentView.photoView)
        }
      }
    }
    if (isLoadingNext) {
      loadingRow {
        id("loadingRow")
      }
    }
  }
}
