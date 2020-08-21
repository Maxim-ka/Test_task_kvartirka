package com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.di.scopelistphoto.ScopeListPhotos
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.OnItemClickListener

@ScopeListPhotos
open class ListPhotoAdapter(downloadable: Downloadable, onItemClickListener: OnItemClickListener<String>?)
    :  BaseListAdapter<String>(downloadable, onItemClickListener) {

    override var list: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhoto {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ItemPhoto(view)
    }

    class ItemPhoto(override val containerView: View) : BaseItem<String>(containerView){

        override fun bind(item: String, downloadable: Downloadable) {
            with(containerView){
                item.also {
                    downloadable.download(it, this)
                }
            }
        }
    }
}