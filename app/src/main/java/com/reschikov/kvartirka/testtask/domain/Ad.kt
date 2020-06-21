package com.reschikov.kvartirka.testtask.domain

import com.reschikov.kvartirka.testtask.data.network.model.Flat

data class Ad(val flat: Flat, val currency : String){

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Ad) return false
        return other.flat == flat && other.currency == currency
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}