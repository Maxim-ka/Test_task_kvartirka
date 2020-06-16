package com.reschikov.kvartirka.testtask.di.scopelistflats

import com.reschikov.kvartirka.testtask.ui.fragments.ListFlatsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@ScopeListFlats
@Subcomponent(modules = [FlatsAdapterModule::class])
interface ListFlatsComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance fun getListFlatsFragment(fragment: ListFlatsFragment) : Builder
        fun listAdapterModule(flatsAdapterModule: FlatsAdapterModule) : Builder
        fun build() : ListFlatsComponent
    }

    fun inject(fragment: ListFlatsFragment)
}