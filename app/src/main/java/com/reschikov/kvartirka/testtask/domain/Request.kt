package com.reschikov.kvartirka.testtask.domain

import com.reschikov.kvartirka.testtask.data.network.model.City
import com.reschikov.kvartirka.testtask.data.network.model.Point

data class Request(val width : Int,
                   val height : Int,
                   val point: Point,
                   val city: City
)