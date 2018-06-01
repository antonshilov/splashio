package io.github.antonshilov.splashio

import android.content.Context
import android.support.constraint.ConstraintSet
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.splashio.api.Photo
import kotlinx.android.synthetic.main.item_photo.view.*
import timber.log.Timber

class PhotoAdapter(val context: Context, val photos: List<Photo>) : RecyclerView.Adapter<PhotoViewHolder>() {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    return PhotoViewHolder(layoutInflater.inflate(R.layout.item_photo, parent, false))
  }

  private val set = ConstraintSet()


  override fun getItemCount() = photos.size

  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    val photo = photos[position]
    Timber.d(photo.toString())
    val url = photos[position].url
    Timber.d("Photo url = %s", url)
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(holder.photo)

    val ratio = String.format("%d:%d", photo.width, photo.height)
    set.clone(holder.constraint)
    set.setDimensionRatio(holder.photo.id, ratio)
    set.applyTo(holder.constraint)
  }

}

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val photo = itemView.photo!!
  val constraint = itemView.parentContsraint!!
}
