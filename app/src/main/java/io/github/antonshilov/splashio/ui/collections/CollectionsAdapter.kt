package io.github.antonshilov.splashio.ui.collections

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.splashio.GlideApp
import io.github.antonshilov.splashio.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_collection.*

class CollectionsAdapter : ListAdapter<Collection, CollectionsAdapter.CollectionViewHolder>(CollectionDiffCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return CollectionViewHolder(inflater.inflate(R.layout.item_collection, parent, false))
  }

  override fun onBindViewHolder(vh: CollectionViewHolder, position: Int) {
    vh.bind(getItem(position))
  }

  inner class CollectionViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(collection: Collection) {
      GlideApp.with(containerView)
        .load(collection.coverPhoto.urls.small)
        .centerCrop()
        .into(cover)
      title.text = collection.title
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


