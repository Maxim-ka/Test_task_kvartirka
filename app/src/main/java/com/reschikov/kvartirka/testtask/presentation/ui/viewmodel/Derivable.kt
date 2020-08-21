package com.reschikov.kvartirka.testtask.presentation.ui.viewmodel

import com.reschikov.kvartirka.testtask.domain.enteries.Reply
import com.reschikov.kvartirka.testtask.domain.enteries.Request

interface Derivable {
    suspend fun requestListOfAds(request: Request) : Reply
    fun terminate()
}