package com.reschikov.kvartirka.testtask.ui

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.reschikov.kvartirka.testtask.domain.Ad

interface Updateable {
    fun getNameCity() : LiveData<String>
    fun getFlats() : LiveData<PagingData<Ad>>
    fun getFlat() : LiveData<Ad>
    fun setError(e : Throwable)
    fun getError() : LiveData<Throwable?>
    fun isVisibleProgress() : LiveData<Boolean>
    fun getListOfAbs(widthPx : Int, heightPx : Int,  isLocal: Boolean)
//    fun createLiveDataPagedList() : LiveData<PagingData<Ad>>
    fun toChoose(item: Ad)
}