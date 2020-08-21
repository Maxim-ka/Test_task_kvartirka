package com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters

import android.view.ViewGroup
import com.reschikov.kvartirka.testtask.di.scopephoto.ScopePhoto

@ScopePhoto
class PhotoAdapter(downloadable: Downloadable) : ListPhotoAdapter(downloadable, null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhoto {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        return holder
    }
}