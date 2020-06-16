package com.reschikov.kvartirka.testtask.data.network.request

import com.reschikov.kvartirka.testtask.data.network.model.ReplyCities
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiBetaKvartirkaPro {

    @GET("cities")
    fun getCities(@Header("X-Client-ID") clientID : String,
                       @Header("X-Device-ID") deviceID : String,
                       @Header("X-Device-OS") deviceOS : String,
                       @Header("X-ApplicationVersion") applicationVersion : String,
                       @Query("empty") empty : Boolean,
                       @Query("lat") lat : Double,
                       @Query("lng") Lng : Double) : Call<ReplyCities>

    @GET("flats")
    fun getFlats(@Header("X-Client-ID") clientID : String,
                   @Header("X-Device-ID") deviceID : String,
                   @Header("X-Device-OS") deviceOS : String,
                   @Header("X-ApplicationVersion") applicationVersion : String,
                   @Query("device_screen_width ") widthPx : Int,
                   @Query("device_screen_height") heightPx : Int,
                   @Query("point_lng") lng : Double,
                   @Query("point_lat") lat : Double,
                   @Query("city_id ") cityId  : Int) : Call<ReplyFlats>
}