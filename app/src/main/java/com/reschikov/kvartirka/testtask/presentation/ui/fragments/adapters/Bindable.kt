package com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters

interface Bindable<T> {
    fun  bind(item : T, downloadable: Downloadable)
}