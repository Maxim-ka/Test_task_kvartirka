package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.ui.fragments.OnItemClickListener
import kotlinx.android.synthetic.main.item_flat.view.*
import kotlinx.coroutines.Dispatchers

class ListFlatsAdapter (private val downloadable: Downloadable,
                        diffCallback : DiffUtil.ItemCallback<Flat>,
                        onItemClickListener: OnItemClickListener)
    :  PagingDataAdapter<Flat, ListFlatsAdapter.ItemFlat>(diffCallback){

    override fun getItemCount(): Int {
        val c = super.getItemCount()
        Log.i("TAG getItemCount", c.toString())
        return c
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFlat {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_flat, parent, false)
        Log.i("TAG ", "onCreateViewHolder")
        return ItemFlat(view)
    }

    override fun onBindViewHolder(holder: ItemFlat, position: Int) {
        Log.i("TAG onBindViewHolder", getItem(position).toString())
        getItem(position)?.let { holder.bind(it, downloadable) }
    }

    class ItemFlat(private val view: View) : RecyclerView.ViewHolder(view), Bindable<Flat>{

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