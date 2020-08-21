package com.reschikov.kvartirka.testtask.di

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.lifecycle.LiveData
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.CommonStatusCodes
import com.reschikov.kvartirka.testtask.data.DefinableCoordinates
import com.reschikov.kvartirka.testtask.data.Requestable
import com.reschikov.kvartirka.testtask.data.network.CheckNetWork
import com.reschikov.kvartirka.testtask.data.network.tsl.ClientOkHttp
import com.reschikov.kvartirka.testtask.data.network.coordinatedeterminants.AndroidCoordinateDeterminant
import com.reschikov.kvartirka.testtask.data.network.coordinatedeterminants.GoogleCoordinateDeterminant
import com.reschikov.kvartirka.testtask.data.network.loader.LoaderImage
import com.reschikov.kvartirka.testtask.data.network.request.ApiBetaKvartirkaPro
import com.reschikov.kvartirka.testtask.data.network.requestprovider.ActivateableTLS
import com.reschikov.kvartirka.testtask.data.network.requestprovider.KvartirkaProvider
import com.reschikov.kvartirka.testtask.data.network.requestprovider.KvartirkaRetrofit
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.Downloadable
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class NetworkModule{

    companion object{

        @SuppressLint("JvmStaticProvidesInObjectDetector")
        @Singleton
        @Provides
        @JvmStatic
        fun provideActivateableTLS(context: Context) : ActivateableTLS? {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                ClientOkHttp(context)
            } else {
                null
            }
        }

        @SuppressLint("JvmStaticProvidesInObjectDetector")
        @Singleton
        @Provides
        @JvmStatic
        fun provideApiBetaKvartirkaPro(activateableTLS: ActivateableTLS?) : ApiBetaKvartirkaPro {
            return KvartirkaRetrofit(activateableTLS)
                .getRetrofit()
                .create(ApiBetaKvartirkaPro::class.java)
        }

        @SuppressLint("JvmStaticProvidesInObjectDetector")
        @Singleton
        @Provides
        @JvmStatic
        fun providePicasso(activateableTLS: ActivateableTLS?) : Picasso {
            return  activateableTLS?.let {
                Picasso.Builder(it.getContext())
                    .downloader(OkHttp3Downloader(it.getClient()))
                    .indicatorsEnabled(true)
                    .build()
                } ?: run {
                    Picasso.get()
                }
        }

        @SuppressLint("JvmStaticProvidesInObjectDetector")
        @JvmStatic
        @Provides
        fun provideDefinableCoordinates(context: Context) : DefinableCoordinates {
            return if (GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(context) == CommonStatusCodes.SUCCESS) {
                GoogleCoordinateDeterminant(context)
            } else {
                AndroidCoordinateDeterminant(context)
            }
        }

        @SuppressLint("JvmStaticProvidesInObjectDetector")
        @JvmStatic
        @Singleton
        @Provides
        fun provideCheckNetWork(context: Context) : LiveData<String?> {
            return CheckNetWork(context)
        }
    }

    @Singleton
    @Binds
    abstract fun bindRequestable(provider: KvartirkaProvider) : Requestable

    @Singleton
    @Binds
    abstract fun bindDownloadable(loaderImage: LoaderImage) : Downloadable
}