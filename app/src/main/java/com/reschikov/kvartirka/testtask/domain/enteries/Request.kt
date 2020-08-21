package com.reschikov.kvartirka.testtask.domain.enteries

data class Request(val sizePx: SizePx,
                   var point: Point?,
                   var city: City?,
                   var meta: Meta)