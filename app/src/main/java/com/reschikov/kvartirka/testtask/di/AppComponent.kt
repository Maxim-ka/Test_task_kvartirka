package com.reschikov.kvartirka.testtask.di

import android.content.Context
import com.reschikov.kvartirka.testtask.di.scopelistflats.ListFlatsComponent
import com.reschikov.kvartirka.testtask.di.scopelistphoto.ListPhotosComponent
import com.reschikov.kvartirka.testtask.di.scopephoto.PhotoComponent
import com.reschikov.kvartirka.testtask.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, AppModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun getContext(context: Context) : Builder
        fun build() : AppComponent
    }
    fun listFlatsComponentBuilder() : ListFlatsComponent.Builder
    fun listPhotosComponentBuilder() : ListPhotosComponent.Builder
    fun photoComponentBuilder() : PhotoComponent.Builder
    fun inject(activity: MainActivity)
}