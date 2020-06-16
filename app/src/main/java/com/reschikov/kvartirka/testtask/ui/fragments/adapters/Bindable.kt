package com.reschikov.kvartirka.testtask.ui.fragments.adapters

interface Bindable<T> {
    fun  bind(item : T, downloadable: Downloadable)
}