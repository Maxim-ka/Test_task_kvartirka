package com.reschikov.kvartirka.testtask.ui.viewmodel

interface Dispatchable {
    fun getDerivable() : Derivable?
    fun setNameCity(name : String)
    fun getInitRequest() : Triple<Int, Int, Boolean>?
    fun setVisibleProgress(isVisible : Boolean)
}