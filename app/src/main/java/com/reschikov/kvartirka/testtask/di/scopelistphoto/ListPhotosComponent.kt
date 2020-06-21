package com.reschikov.kvartirka.testtask.di.scopelistphoto

import com.reschikov.kvartirka.testtask.ui.fragments.FlatFragment
import dagger.BindsInstance
import dagger.Subcomponent

@ScopeListPhotos
@Subcomponent(modules = [PhotosAdapterModule::class])
interface ListPhotosComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun getFlatFragment(fragment: FlatFragment) : Builder
        fun listAdapterModule(photoAdapterModule: PhotosAdapterModule) : Builder
        fun build() : ListPhotosComponent
    }

    fun inject(fragment: FlatFragment)
}