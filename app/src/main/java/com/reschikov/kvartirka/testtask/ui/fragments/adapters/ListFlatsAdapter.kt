package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.ui.fragments.OnItemClickListener
import kotlinx.android.synthetic.main.item_flat.view.*

class ListFlatsAdapter (downloadable: Downloadable,
                        onItemClickListener: OnItemClickListener)
    :  BaseListAdapter<Flat>(downloadable, onItemClickListener){

    override var list: List<Flat> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFlat {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_flat, parent, false)
        return ItemFlat(view)
    }

    class ItemFlat(private val view: View) : BaseItem<Flat>(view){

        override fun bind(item: Flat, downloadable: Downloadable) {
            with(view){
                item.also {
                    downloadable.download(it.photo.url, iv_default_photo)
                    tv_title.text = it.title
                    tv_address.text = it.address
                    tv_price.text = it.prices.getPrice(view.context)
                }
            }
        }
    }
}