package com.reschikov.kvartirka.testtask.domain

import com.reschikov.kvartirka.testtask.data.network.model.City

data class Request(val width : Int,
                   val height : Int,
                   val city: City)