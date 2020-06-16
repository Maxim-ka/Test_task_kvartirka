package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.data.network.model.Photo
import com.reschikov.kvartirka.testtask.ui.fragments.OnItemClickListener
import kotlinx.android.synthetic.main.item_photo.view.*

open class ListPhotoAdapter(downloadable: Downloadable, onItemClickListener: OnItemClickListener?)
    :  BaseListAdapter<Photo>(downloadable, onItemClickListener) {

    override var list: List<Photo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhoto {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ItemPhoto(view)
    }

    class ItemPhoto(private val view: View) : BaseItem<Photo>(view){

        override fun bind(item: Photo, downloadable: Downloadable) {
            with(view){
                item.also {
                    downloadable.download(it.url, iv_photo)
                }
            }
        }
    }
}