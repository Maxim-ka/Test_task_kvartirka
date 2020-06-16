package com.reschikov.kvartirka.testtask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reschikov.kvartirka.testtask.ui.viewmodel.MainViewModel
import com.reschikov.kvartirka.testtask.ui.Updateable
import com.reschikov.kvartirka.testtask.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ViewModelModule {

    @Singleton
    @Binds
    fun bindUpdateable(viewModel: MainViewModel) : Updateable

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindViewModel(viewModel: MainViewModel) : ViewModel

    @Singleton
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory) : ViewModelProvider.Factory

}