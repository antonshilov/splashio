package io.github.antonshilov.splashio.ui.featured

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.antonshilov.domain.PhotoRepo
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_photo.view.*
import timber.log.Timber

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val photoView = itemView.photo!!
  fun bind(photo: Photo, clickListener: PhotoCardClickListener) {
    val url = photo.urls.regular
    val thumbnailRequest = GlideApp.with(itemView)
      .load(photo.urls.thumb)
      .transition(DrawableTransitionOptions.withCrossFade())
    Glide.with(itemView)
      .load(url)
      .thumbnail(thumbnailRequest)
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(photoView)
    photoView.setAspectRatio(photo.width, photo.height)
    photoView.transitionName = photo.id
    photoView.setOnClickListener {
      clickListener?.invoke(photo, photoView)
    }
  }
}
typealias PhotoCardClickListener = ((photo: Photo, sharedImageView: ImageView) -> Unit)?

class PhotoAdapter : PagedListAdapter<Photo, PhotoViewHolder>(DIFF_CALLBACK) {
  var onItemClickListener: PhotoCardClickListener = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
    return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false))
  }

  override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
    val photo = getItem(position)!!
    holder.bind(photo, onItemClickListener)
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

enum class Status {
  RUNNING,
  SUCCESS,
  FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
  val status: Status,
  val msg: String? = null
) {
  companion object {
    val LOADED = NetworkState(Status.SUCCESS)
    val LOADING = NetworkState(Status.RUNNING)
    fun error(msg: String?) = NetworkState(Status.FAILED, msg)
  }
}

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 */
class PhotoDataSourceFactory(
  private val api: PhotoRepo,
  private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Photo>() {
  val sourceLiveData = MutableLiveData<PhotoDataSource>()
  override fun create(): DataSource<Int, Photo> {
    val source = PhotoDataSource(api, compositeDisposable)
    sourceLiveData.postValue(source)
    return source
  }
}

class PhotoDataSource(private val api: PhotoRepo, private val compositeDisposable: CompositeDisposable) :
  PageKeyedDataSource<Int, Photo>() {

  val networkState = MutableLiveData<NetworkState>()

  override fun loadInitial(
    params: LoadInitialParams<Int>,
    callback: LoadInitialCallback<Int,
      Photo>
  ) {
    networkState.postValue(NetworkState.LOADING)
    api.getLatestPhotos(pageSize = params.requestedLoadSize, page = 1)
      .subscribeBy(
        onSuccess = {
          callback.onResult(it, 1, 2)
          networkState.postValue(NetworkState.LOADED)
          Timber.d("Initial load of curated photos succeed")
        },
        onError = {
          networkState.postValue(NetworkState.error(it.message ?: "unknown error"))
          Timber.e(it, "Initial load of curated photos failed")
        }
      ).addTo(compositeDisposable)
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    api.getLatestPhotos(pageSize = params.requestedLoadSize, page = params.key)
      .subscribeBy(
        onSuccess = {
          Timber.d("Load of curated photos succeed")
          callback.onResult(it, params.key + 1)
        },
        onError = {
          Timber.e(it, "Load of curated photos has failed")
        }
      ).addTo(compositeDisposable)
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    //    No need to load before
  }
}
