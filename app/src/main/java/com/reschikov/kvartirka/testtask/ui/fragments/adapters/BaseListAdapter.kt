package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.reschikov.kvartirka.testtask.ui.fragments.OnItemClickListener

abstract class BaseListAdapter<T>(private val downloadable: Downloadable,
                                  private val onItemClickListener: OnItemClickListener<T>?)
    : RecyclerView.Adapter<BaseListAdapter.BaseItem<T>>() {

    abstract var list: List<T>

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseItem<T>, position: Int) {
        holder.bind(list[position], downloadable)
    }

    override fun onViewAttachedToWindow(holder: BaseItem<T>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener{onItemClickListener?.onItemClick(list[holder.bindingAdapterPosition])}
    }

    override fun onViewDetachedFromWindow(holder: BaseItem<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    abstract  class BaseItem<T>(view: View) : RecyclerView.ViewHolder(view), Bindable<T>
}