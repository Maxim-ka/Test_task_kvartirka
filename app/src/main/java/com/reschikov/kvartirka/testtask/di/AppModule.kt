package com.reschikov.kvartirka.testtask.di

import com.reschikov.kvartirka.testtask.data.Repository
import com.reschikov.kvartirka.testtask.ui.viewmodel.Derivable
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppModule {

    @Binds
    @Singleton
    fun bindDerivable(repository: Repository) : Derivable
}