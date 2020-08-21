package com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.di.scopelistflats.ScopeListFlats
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.OnItemClickListener
import kotlinx.android.synthetic.main.item_flat.view.*

@ScopeListFlats
class ListFlatsAdapter (private val downloadable: Downloadable,
                        diffCallback : DiffUtil.ItemCallback<Ad>,
                        private val onItemClickListener: OnItemClickListener<Ad>)
    :  PagingDataAdapter<Ad, ListFlatsAdapter.ItemFlat>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFlat {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_flat, parent, false)
        return ItemFlat(view)
    }

    override fun onBindViewHolder(holder: ItemFlat, position: Int) {
        getItem(position)?.let { holder.bind(it, downloadable) }
    }

    override fun onViewAttachedToWindow(holder: ItemFlat) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener{
            getItem(holder.bindingAdapterPosition)?.let { onItemClickListener.onItemClick(it) }
        }
    }

    override fun onViewDetachedFromWindow(holder: ItemFlat) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    class ItemFlat(private val view: View) : RecyclerView.ViewHolder(view), Bindable<Ad>{

        override fun bind(item: Ad, downloadable: Downloadable) {
            with(view){
                item.also {
                    downloadable.download(it.mainPhotoUrl, view)
                    tv_title.text = it.title
                    tv_address.text = it.address
                    tv_price.text = it.price
                }
            }
        }
    }
}