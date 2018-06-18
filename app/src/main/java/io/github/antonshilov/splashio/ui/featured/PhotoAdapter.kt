package io.github.antonshilov.splashio.ui.featured

import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.constraint.ConstraintSet
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.api.UnsplashApi
import io.github.antonshilov.splashio.api.model.Photo
import kotlinx.android.synthetic.main.item_photo.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val photo = itemView.photo!!
  val constraint = itemView.parentContsraint!!
}

class PhotoAdapter(val context: Context) : PagedListAdapter<Photo, PhotoViewHolder>(DIFF_CALLBACK) {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
  private val set = ConstraintSet()
  var onItemClickListener: ((photo: Photo) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    return PhotoViewHolder(layoutInflater.inflate(R.layout.item_photo, parent, false))
  }

  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    val photo = getItem(position)!!
    val url = photo.url
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(holder.photo)

    // apply aspect ratio to the image
    val ratio = String.format("%d:%d", photo.width, photo.height)
    set.clone(holder.constraint)
    set.setDimensionRatio(holder.photo.id, ratio)
    set.applyTo(holder.constraint)
    holder.constraint.setOnClickListener {
      onItemClickListener?.invoke(photo)
    }
  }

  companion object {
    @JvmStatic
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

class PhotoDataSource(val api: UnsplashApi) : PageKeyedDataSource<Int, Photo>() {
  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int,
      Photo>) {
    api.getCuratedPhotos(limit = params.requestedLoadSize).enqueue(object : Callback<List<Photo>> {
      override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
        Timber.d(t, "Initial load of curated photos has failed")
      }

      override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
        Timber.d("Initial load of curated photos succeed")
        val images = response.body()!!
        callback.onResult(images, 1, 2)
      }

    })
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    api.getCuratedPhotos(limit = params.requestedLoadSize, page = params.key).enqueue(object :
        Callback<List<Photo>> {
      override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
        Timber.d(t, "Load of curated photos has failed")
      }

      override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
        Timber.d("Load of curated photos succeed")
        val images = response.body()!!
        callback.onResult(images, params.key + 1)
      }

    })
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    //    No need to load before
  }
}
