package io.github.antonshilov.splashio.ui.collections

import android.arch.paging.PagedListAdapter
import android.content.res.Resources
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
      bindInnerPhotos(collection.previewPhotosUrls)
      title.text = collection.title
      val text = getDescription(collection, containerView.resources)
      description.text = text

    }

    private fun bindCoverPhoto(photoEntity: Photo) {
      cover.loadPhoto(photoEntity)
    }

    private fun bindInnerPhotos(urls: List<String>?) {
      if (urls == null) return
      first_inner_image.loadPhoto(urls.getOrNull(1))
      second_inner_image.loadPhoto(urls.getOrNull(2))
      third_inner_image.loadPhoto(urls.getOrNull(3))
    }
  }

  private fun getDescription(collection: Collection, res: Resources) : String {
    val name = collection.userName
    val count = collection.totalPhotos
    return if(name != null) {
      res.getQuantityString(R.plurals.collection_description_plurals_with_curator, count, count, name)
    } else{
      res.getQuantityString(R.plurals.collection_description_plurals_without_curator, count, count, name)
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
