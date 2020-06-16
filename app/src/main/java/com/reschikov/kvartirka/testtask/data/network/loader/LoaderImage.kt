package com.reschikov.kvartirka.testtask.data.network.loader

import android.content.Context
import android.widget.ImageView
import com.reschikov.kvartirka.testtask.data.network.requestprovider.ActivateableTLS
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.Downloadable
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import javax.inject.Inject
import javax.inject.Singleton

private const val BAD_HOST = "media.beta.kvartirka.pro"
private const val GOOD_HOST = "media.kvartirka.com"

@Singleton
class LoaderImage @Inject constructor(activateableTLS: ActivateableTLS?,
                                      context: Context?): Downloadable{

    private val picasso: Picasso = if (activateableTLS != null && context != null) {
         Picasso.Builder(context)
            .downloader(OkHttp3Downloader(activateableTLS.getClient()))
            .indicatorsEnabled(true)
            .build()
    } else {
        Picasso.get()
    }

    override fun download(url: String, imageView: ImageView) {
        picasso.load(replace(url)).fit().into(imageView)
    }

    private fun replace(url : String) : String{
        if (url.contains(BAD_HOST, true)) return url.replace(
            BAD_HOST,
            GOOD_HOST, true)
        return url
    }
}