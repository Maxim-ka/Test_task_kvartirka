package com.reschikov.kvartirka.testtask.ui.viewmodel

import androidx.lifecycle.LiveData
import com.reschikov.kvartirka.testtask.data.network.model.Flat

interface Derivable {
    fun getResult() : LiveData<Triple<String?, List<Flat>?, Throwable?>>
    fun isProgress() : LiveData<Boolean>
    fun requestListOfAds(widthPx : Int, heightPx : Int, isLocal: Boolean)
    fun terminate()
}