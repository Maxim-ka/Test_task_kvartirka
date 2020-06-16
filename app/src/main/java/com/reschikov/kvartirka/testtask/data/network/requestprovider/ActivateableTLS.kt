package com.reschikov.kvartirka.testtask.data.network.requestprovider

import okhttp3.OkHttpClient

interface ActivateableTLS {
    fun getClient() : OkHttpClient
}