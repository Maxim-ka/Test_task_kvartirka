package com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters

import android.view.View

interface Downloadable {
    fun download(url: String, view: View)
}