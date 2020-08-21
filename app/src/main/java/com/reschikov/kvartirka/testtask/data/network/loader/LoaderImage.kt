package com.reschikov.kvartirka.testtask.data.network.loader

import android.view.View
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.Downloadable
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.block_image.view.*
import kotlinx.android.synthetic.main.block_progress.view.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoaderImage @Inject constructor(private val picasso: Picasso): Downloadable{

    override fun download(url: String, view: View) {
        view.pb_circle.visibility = View.VISIBLE
        picasso.load(url).fit().into(view.iv_photo, object : Callback{
            override fun onSuccess() {
                view.pb_circle.visibility = View.GONE
                picasso.cancelRequest(view.iv_photo)
            }

            override fun onError(e: Exception?) {
                view.pb_circle.visibility = View.GONE
                view.iv_photo.contentDescription = e?.message ?: e.toString()
                picasso.cancelRequest(view.iv_photo)
            }
        })
    }
}