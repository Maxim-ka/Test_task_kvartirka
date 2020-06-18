package com.reschikov.kvartirka.testtask.ui.viewmodel

import com.reschikov.kvartirka.testtask.domain.Request
import kotlinx.coroutines.CoroutineScope

interface Dispatchable {
    fun getDerivable() : Derivable
    fun setNameCity(name : String)
    fun setError(err : Throwable?)
    fun saveRequest(request : Request)
    fun getRequest() : Request
}