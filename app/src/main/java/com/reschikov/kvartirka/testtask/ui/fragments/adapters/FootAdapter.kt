package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reschikov.kvartirka.testtask.R
import kotlinx.android.synthetic.main.block_item_footer.view.*

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
                val view = LayoutInflater.from(parent.context).inflate(R.layout.block_item_footer, parent, false)
                return FooterViewHolder(view)
            }
        }


        fun bind(loadState: LoadState) {
            with(itemView){
                tv_error.visibility = if (loadState is LoadState.Error) {
                    tv_error.text = loadState.error.message
                    View.VISIBLE
                } else View.GONE
                but_retry.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
                pb_progress.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE

            }
        }
    }
}