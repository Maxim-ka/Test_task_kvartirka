package com.reschikov.kvartirka.testtask.data.network.requestprovider

import android.content.Context
import okhttp3.OkHttpClient

interface ActivateableTLS {
    fun getClient() : OkHttpClient
    fun getContext() : Context
}