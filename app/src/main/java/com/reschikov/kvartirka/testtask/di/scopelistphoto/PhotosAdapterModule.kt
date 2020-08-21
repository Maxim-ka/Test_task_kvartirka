package com.reschikov.kvartirka.testtask.di.scopelistphoto

import com.reschikov.kvartirka.testtask.presentation.ui.fragments.FlatFragment
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.Downloadable
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.ListPhotoAdapter
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