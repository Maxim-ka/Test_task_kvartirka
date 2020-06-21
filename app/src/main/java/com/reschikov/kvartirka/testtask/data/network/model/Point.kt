package com.reschikov.kvartirka.testtask.data.network.model

import com.google.gson.annotations.SerializedName

data class Point(@SerializedName("point_lat") val lat : Double,
                 @SerializedName("point_lng") val lon : Double)