package com.reschikov.kvartirka.testtask.di.scopelistphoto

import com.reschikov.kvartirka.testtask.ui.fragments.FlatFragment
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.Downloadable
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.ListPhotoAdapter
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class PhotosAdapterModule {

    @ScopeListPhotos
    @Provides
    fun provideListPhotoAdapter(downloadable: Downloadable, fragment: FlatFragment) : ListPhotoAdapter{
        return ListPhotoAdapter(downloadable, fragment)
    }
}