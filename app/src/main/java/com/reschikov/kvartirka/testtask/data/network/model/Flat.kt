package com.reschikov.kvartirka.testtask.data.network.model

import com.google.gson.annotations.SerializedName

data class Flat(private val id : Int,
                @SerializedName("city_id") private val cityId : Int,
                @SerializedName("building_type") val type : String,
                val rooms : Int,
                val title :String,
                val address : String,
                val prices : Prices,
                @SerializedName("photo_default") val photo: Photo,
                val photos : List<Photo>)