package io.github.antonshilov.splashio.ui

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.GlideApp

fun ImageView.loadPhoto(photo: Photo) {
  val url = photo.urls.regular
  val color = ColorDrawable(photo.color.toColorInt())
  val thumbnailRequest = GlideApp.with(this)
    .load(photo.urls.thumb)
    .transition(DrawableTransitionOptions.withCrossFade())
  GlideApp.with(this)
    .load(url)
    .thumbnail(thumbnailRequest)
    .placeholder(color)
    .centerCrop()
    .transition(DrawableTransitionOptions.withCrossFade())
    .into(this)
}
