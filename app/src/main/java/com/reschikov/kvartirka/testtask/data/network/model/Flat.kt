package com.reschikov.kvartirka.testtask.data.network.model

import com.google.gson.annotations.SerializedName

data class Flat(val id : Int,
                @SerializedName("city_id") val cityId : Int,
                @SerializedName("building_type") val type : String,
                val rooms : Int,
                val title :String,
                val address : String,
                val prices : Prices,
                @SerializedName("photo_default") val photo: Photo,
                val photos : List<Photo>)  {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Flat) return false
        return other.id == id && other.cityId == cityId && other.title == title && other.type == type &&
            other.rooms == rooms && other.address == address && other.prices == prices
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}