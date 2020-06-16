package com.reschikov.kvartirka.testtask.data.network.requestprovider

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_SERVER = "https://api.beta.kvartirka.pro/client/1.4/"

class KvartirkaRetrofit(private val activateableTLS: ActivateableTLS?) {

    fun getRetrofit() : Retrofit =  if(activateableTLS != null){
        Retrofit
            .Builder()
            .client(activateableTLS.getClient())
            .baseUrl(URL_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    } else {
        Retrofit
            .Builder()
            .baseUrl(URL_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}