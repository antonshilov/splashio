package io.github.antonshilov.splashio

import android.content.Context
import android.support.constraint.ConstraintSet
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.splashio.api.Photo
import kotlinx.android.synthetic.main.item_photo.view.*
import timber.log.Timber

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val photo = itemView.photo!!
  val constraint = itemView.parentContsraint!!
}

class PhotoAdapter(val context: Context) : ListAdapter<Photo, PhotoViewHolder>(DIFF_CALLBACK) {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
  private val set = ConstraintSet()
  var onItemClickListener: ((photo: Photo) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    return PhotoViewHolder(layoutInflater.inflate(R.layout.item_photo, parent, false))
  }

  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    val photo = getItem(position)
    Timber.d(photo.toString())
    val url = photo.url
    Timber.d("Photo url = %s", url)
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(holder.photo)

    val ratio = String.format("%d:%d", photo.width, photo.height)
    set.clone(holder.constraint)
    set.setDimensionRatio(holder.photo.id, ratio)
    set.applyTo(holder.constraint)
    holder.constraint.setOnClickListener({
      onItemClickListener?.invoke(photo)
    })
  }

  companion object {
    val DIFF_CALLBACK: DiffUtil.ItemCallback<Photo> = object : DiffUtil.ItemCallback<Photo>() {
      override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
      }
    }
  }

}
