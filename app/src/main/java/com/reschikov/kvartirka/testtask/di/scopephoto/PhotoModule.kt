package com.reschikov.kvartirka.testtask.di.scopephoto


import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.Downloadable
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.PhotoAdapter
import dagger.Module
import dagger.Provides

@Module
class PhotoModule {

    @ScopePhoto
    @Provides
    fun providePhotoAdapter(downloadable: Downloadable) : PhotoAdapter {
        return PhotoAdapter(downloadable)
    }
}