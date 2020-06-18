package com.reschikov.kvartirka.testtask.ui.viewmodel

import androidx.lifecycle.LiveData
import com.reschikov.kvartirka.testtask.domain.Meta
import com.reschikov.kvartirka.testtask.domain.Reply
import com.reschikov.kvartirka.testtask.domain.Request

interface Derivable {
    fun getStateNet() : LiveData<String?>
    fun isProgress() : LiveData<Boolean>
    suspend fun requestListOfAds(widthPx : Int, heightPx : Int, isLocal: Boolean, meta: Meta) : Pair<Reply?, Throwable?>
    suspend fun requestListOfAds(request: Request, meta: Meta) : Pair<Reply?, Throwable?>
    fun terminate()
}