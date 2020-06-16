package com.reschikov.kvartirka.testtask.ui.fragments.adapters

import android.widget.ImageView

interface Downloadable {
    fun download(url: String, imageView: ImageView)
}