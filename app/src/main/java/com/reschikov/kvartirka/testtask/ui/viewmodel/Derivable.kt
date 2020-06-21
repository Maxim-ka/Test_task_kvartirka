package com.reschikov.kvartirka.testtask.ui.viewmodel

import com.reschikov.kvartirka.testtask.domain.Meta
import com.reschikov.kvartirka.testtask.domain.Reply
import com.reschikov.kvartirka.testtask.domain.Request

interface Derivable {
    suspend fun requestListOfAds(widthPx : Int, heightPx : Int, isLocal: Boolean, meta: Meta) : Reply
    suspend fun requestListOfAds(request: Request, meta: Meta) : Reply
    fun terminate()
}