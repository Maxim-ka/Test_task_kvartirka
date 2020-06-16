package com.reschikov.kvartirka.testtask.ui

import androidx.lifecycle.LiveData
import com.reschikov.kvartirka.testtask.data.network.model.Flat

interface Updateable {
    fun getNameCity() : LiveData<String>
    fun getFlats() : LiveData<List<Flat>>
    fun getFlat() : LiveData<Flat>
    fun getError() : LiveData<Throwable?>
    fun isVisibleProgress() : LiveData<Boolean>
    fun getListOfAbs(widthPx : Int, heightPx : Int,  isLocal: Boolean)
    fun toChoose(position: Int)
}