package com.reschikov.kvartirka.testtask.presentation.ui

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.domain.enteries.SizePx

interface Updateable {

    fun getNameCity() : LiveData<String>
    fun getFlats() : LiveData<PagingData<Ad>>
    fun getFlat() : LiveData<Ad>
    fun setRequestParameters(sizePx: SizePx, isLocal: Boolean)
    fun toChoose(item: Ad)
}