package com.reschikov.kvartirka.testtask.di.scopelistflats

import androidx.recyclerview.widget.DiffUtil
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.ListFlatsFragment
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.Downloadable
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.ListFlatsAdapter
import dagger.Module
import dagger.Provides

@Module
class FlatsAdapterModule {

    companion object {
        @JvmStatic
        private val diffCallback = object : DiffUtil.ItemCallback<Ad>() {
            override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean {
                return  oldItem == newItem
            }
        }
    }

    @ScopeListFlats
    @Provides
    fun provideListFlatsAdapter(downloadable: Downloadable, fragment: ListFlatsFragment) : ListFlatsAdapter {
        return ListFlatsAdapter(downloadable, diffCallback, fragment)
    }
}