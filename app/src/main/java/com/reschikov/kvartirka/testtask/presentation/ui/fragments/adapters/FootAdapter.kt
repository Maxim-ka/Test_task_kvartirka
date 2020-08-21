package com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reschikov.kvartirka.testtask.R
import kotlinx.android.synthetic.main.item_footer.view.*
import kotlinx.android.synthetic.main.block_progress.view.*

class FootAdapter(private val retry : () -> Unit) : LoadStateAdapter<FootAdapter.FooterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        return FooterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
        holder.itemView.but_retry.setOnClickListener{retry.invoke()}
    }

    class FooterViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        companion object{
            @JvmStatic
            fun create(parent: ViewGroup) : FooterViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
                return FooterViewHolder(view)
            }
        }

        fun bind(loadState: LoadState) {
            with(itemView){
                loadState.also {
                    tv_error.visibility = if (it is LoadState.Error) {
                        it.error.also { err ->  tv_error.text = err.message ?: err.toString() }
                        View.VISIBLE
                    } else View.GONE
                    but_retry.visibility = if (it is LoadState.Error) View.VISIBLE else View.GONE
                    pb_circle.visibility = if (it is LoadState.Loading) View.VISIBLE else View.GONE
                }
            }
        }
    }
}