package com.reschikov.kvartirka.testtask.di

import android.content.Context
import com.reschikov.kvartirka.testtask.AppTestTask
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ViewModelModule::class, AppModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun getContext(context: Context) : Builder
        fun build() : AppComponent
    }
    fun inject(app : AppTestTask)
}