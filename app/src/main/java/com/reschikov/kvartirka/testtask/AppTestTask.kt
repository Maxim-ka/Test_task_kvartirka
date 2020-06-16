package com.reschikov.kvartirka.testtask

import android.app.Application
import com.reschikov.kvartirka.testtask.di.AppComponent
import com.reschikov.kvartirka.testtask.di.DaggerAppComponent
import com.reschikov.kvartirka.testtask.di.scopelistflats.FlatsAdapterModule
import com.reschikov.kvartirka.testtask.di.scopelistflats.ListFlatsComponent
import com.reschikov.kvartirka.testtask.di.scopelistphoto.ListPhotosComponent
import com.reschikov.kvartirka.testtask.di.scopelistphoto.PhotosAdapterModule
import com.reschikov.kvartirka.testtask.di.scopephoto.PhotoComponent
import com.reschikov.kvartirka.testtask.di.scopephoto.PhotoModule
import com.reschikov.kvartirka.testtask.ui.fragments.FlatFragment
import com.reschikov.kvartirka.testtask.ui.fragments.ListFlatsFragment

class AppTestTask : Application(){

    companion object{
        private lateinit var appDagger: AppTestTask

        @JvmStatic
        fun getAppDagger(): AppTestTask {
            return appDagger
        }
    }

    private lateinit var appComponent: AppComponent
    private var flatsComponent: ListFlatsComponent? = null
    private var photosComponent: ListPhotosComponent? = null
    private var photoComponent: PhotoComponent? = null

    fun getAppComponent() : AppComponent = appComponent

    override fun onCreate() {
        super.onCreate()
        appDagger = this
        appComponent = createAppComponent()
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent
            .builder()
            .getContext(applicationContext)
            .build()
    }

    fun getFlatsComponent(fragment: ListFlatsFragment): ListFlatsComponent {
        if (flatsComponent == null){
            flatsComponent = appComponent
                .listFlatsComponentBuilder()
                .getListFlatsFragment(fragment)
                .listAdapterModule(FlatsAdapterModule())
                .build()
        }
        return flatsComponent!!
    }

    fun getPhotosComponent(fragment: FlatFragment): ListPhotosComponent {
        if (photosComponent == null) {
            photosComponent = appComponent
                .listPhotosComponentBuilder()
                .getFlatFragment(fragment)
                .listAdapterModule(PhotosAdapterModule())
                .build()
        }
        return photosComponent!!
    }

    fun getPhotoComponent(): PhotoComponent {
        if (photoComponent == null) {
            photoComponent = appComponent
                .photoComponentBuilder()
                .photoModule(PhotoModule())
                .build()
        }
        return photoComponent!!
    }

    fun clearFlatsComponent(){
        flatsComponent = null
    }

    fun clearPhotosComponent(){
        photosComponent = null
    }
}