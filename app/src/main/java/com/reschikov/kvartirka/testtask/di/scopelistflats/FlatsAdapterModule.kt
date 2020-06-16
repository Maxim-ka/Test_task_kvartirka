package com.reschikov.kvartirka.testtask.di.scopelistflats

import com.reschikov.kvartirka.testtask.ui.fragments.ListFlatsFragment
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.Downloadable
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.ListFlatsAdapter
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class FlatsAdapterModule {

    @ScopeListFlats
    @Provides
    fun provideListFlatsAdapter(downloadable: Downloadable, fragment: ListFlatsFragment) : ListFlatsAdapter {
        return ListFlatsAdapter(downloadable, fragment)
    }
}