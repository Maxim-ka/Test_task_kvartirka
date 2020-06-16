package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(downloadable: Downloadable) : ListPhotoAdapter(downloadable, null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhoto {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        return holder
    }
}