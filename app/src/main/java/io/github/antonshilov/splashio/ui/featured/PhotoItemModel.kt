package io.github.antonshilov.splashio.ui.featured

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.paging.PagedListEpoxyController
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
      val colorThumbnail = GlideApp.with(photoView)
        .load(ColorDrawable(Color.parseColor(photo.color)))
      val thumbnailRequest = GlideApp.with(photoView)
        .load(photo.urls.thumb)
        .thumbnail(colorThumbnail)
        .transition(DrawableTransitionOptions.withCrossFade())
      Glide.with(photoView)
        .load(photo.urls.regular)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(photoView)
      photoView.setAspectRatio(photo.width, photo.height)
      photoView.setOnClickListener(clickListener)
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

class PhotoController(private val photoCardClickListener: PhotoCardClickListener) : PagedListEpoxyController<Photo>() {
  override fun buildItemModel(currentPosition: Int, item: Photo?): EpoxyModel<*> {
    return PhotoItemModel_()
      .photo(item!!)
      .clickListener { _: PhotoItemModel_?, parentView: Holder?, _: View?, _: Int ->
        photoCardClickListener.invoke(item, parentView!!.photoView)
      }
      .id(item.id)
  }
}
