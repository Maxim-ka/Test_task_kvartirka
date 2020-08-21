package com.reschikov.kvartirka.testtask.di

import com.reschikov.kvartirka.testtask.data.Mapping
import com.reschikov.kvartirka.testtask.data.Repository
import com.reschikov.kvartirka.testtask.data.Transformable
import com.reschikov.kvartirka.testtask.di.scopelistflats.FlatsAdapterModule
import com.reschikov.kvartirka.testtask.di.scopelistflats.ScopeListFlats
import com.reschikov.kvartirka.testtask.di.scopelistphoto.PhotosAdapterModule
import com.reschikov.kvartirka.testtask.di.scopelistphoto.ScopeListPhotos
import com.reschikov.kvartirka.testtask.di.scopephoto.PhotoModule
import com.reschikov.kvartirka.testtask.di.scopephoto.ScopePhoto
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.FlatFragment
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.ListFlatsFragment
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.PhotoFragment
import com.reschikov.kvartirka.testtask.presentation.ui.viewmodel.Derivable
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
interface AppModule {

    @Binds
    @Singleton
    fun bindDerivable(repository: Repository) : Derivable

    @Binds
    fun bindTransformable(mapping: Mapping) : Transformable

    @ScopeListFlats
    @ContributesAndroidInjector(modules = [FlatsAdapterModule::class])
    fun listFlatsFragmentInjector () : ListFlatsFragment

    @ScopeListPhotos
    @ContributesAndroidInjector(modules = [PhotosAdapterModule::class])
    fun flatFragmentInjector () : FlatFragment

    @ScopePhoto
    @ContributesAndroidInjector(modules = [PhotoModule::class])
    fun photoFragmentInjector () : PhotoFragment
}