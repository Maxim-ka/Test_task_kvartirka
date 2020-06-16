package com.reschikov.kvartirka.testtask.di.scopephoto

import com.reschikov.kvartirka.testtask.ui.fragments.PhotoFragment
import dagger.Subcomponent

@ScopePhoto
@Subcomponent(modules = [PhotoModule::class])
interface PhotoComponent {

    @Subcomponent.Builder
    interface Builder {
        fun photoModule(photoModule: PhotoModule) : Builder
        fun build() : PhotoComponent
    }

    fun inject(fragment: PhotoFragment)
}