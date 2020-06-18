package com.reschikov.kvartirka.testtask.ui

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.ui.viewmodel.Derivable

interface Updateable {
    fun getNameCity() : LiveData<String>
    fun getFlats() : LiveData<PagingData<Flat>>
    fun getFlat() : LiveData<Flat>
    fun getError() : LiveData<Throwable?>
    fun isVisibleProgress() : LiveData<Boolean>
    fun getListOfAbs(widthPx : Int, heightPx : Int,  isLocal: Boolean)
    fun toChoose(position: Int)
}