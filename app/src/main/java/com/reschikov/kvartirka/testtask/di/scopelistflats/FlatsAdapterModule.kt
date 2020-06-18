package com.reschikov.kvartirka.testtask.di.scopelistflats

import androidx.recyclerview.widget.DiffUtil
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.ui.fragments.ListFlatsFragment
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.Downloadable
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.ListFlatsAdapter
import dagger.Module
import dagger.Provides

@Module
class FlatsAdapterModule {

    companion object {
        @JvmStatic
        private val diffCallback = object : DiffUtil.ItemCallback<Flat>() {
            override fun areItemsTheSame(oldItem: Flat, newItem: Flat): Boolean {
                return oldItem.cityId == newItem.cityId && oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Flat, newItem: Flat): Boolean {
                return oldItem.run{
                    type == newItem.type && rooms == newItem.rooms && title == newItem.title &&
                            address == newItem.address && prices == newItem.prices
                }
            }
        }
    }

    @ScopeListFlats
    @Provides
    fun provideListFlatsAdapter(downloadable: Downloadable, fragment: ListFlatsFragment) : ListFlatsAdapter {
        return ListFlatsAdapter(downloadable, diffCallback, fragment)
    }
}