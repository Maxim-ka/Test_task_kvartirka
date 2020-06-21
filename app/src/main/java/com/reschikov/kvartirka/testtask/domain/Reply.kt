package com.reschikov.kvartirka.testtask.domain

import com.reschikov.kvartirka.testtask.data.network.model.City
import com.reschikov.kvartirka.testtask.data.network.model.Point

data class Reply(val city: City,
                 val flats: List<Ad>,
                 val point: Point,
                 val meta: Meta)