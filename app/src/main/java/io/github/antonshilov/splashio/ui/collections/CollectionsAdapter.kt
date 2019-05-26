package io.github.antonshilov.splashio.ui.collections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.domain.feed.photos.model.Photo
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.loadPhoto
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_collection.*

class CollectionsAdapter :
  PagedListAdapter<Collection, CollectionsAdapter.CollectionViewHolder>(CollectionDiffCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return CollectionViewHolder(inflater.inflate(R.layout.item_collection, parent, false))
  }

  override fun onBindViewHolder(vh: CollectionViewHolder, position: Int) {
    val collection = getItem(position)
    if (collection != null) {
      // not a placeholder, real collection item
      vh.bind(collection)
    }
  }

  inner class CollectionViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(collection: Collection) {
      bindCoverPhoto(collection.coverPhoto)
      title.text = collection.title
    }

    private fun bindCoverPhoto(photoEntity: Photo) {
      cover.loadPhoto(photoEntity)
    }
  }

  private class CollectionDiffCallback : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(p0: Collection, p1: Collection): Boolean {
      return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: Collection, p1: Collection): Boolean {
      return p0 == p1
    }
  }
}
