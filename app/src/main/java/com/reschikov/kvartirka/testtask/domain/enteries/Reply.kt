package com.reschikov.kvartirka.testtask.domain.enteries

data class Reply(val flats: List<Ad>,
                 val point: Point,
                 val city: City,
                 val meta: Meta)