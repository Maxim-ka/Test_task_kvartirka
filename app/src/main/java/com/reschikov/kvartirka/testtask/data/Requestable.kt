package com.reschikov.kvartirka.testtask.data

import com.reschikov.kvartirka.testtask.domain.enteries.Point
import com.reschikov.kvartirka.testtask.data.network.model.ReplyCities
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import com.reschikov.kvartirka.testtask.domain.enteries.Meta
import com.reschikov.kvartirka.testtask.domain.enteries.SizePx

interface Requestable {

    suspend fun getCurrentCity(lat : Double, lng : Double) : ReplyCities
    suspend fun getListFlats(sizePx: SizePx, point: Point, cityId: Int, meta : Meta) : ReplyFlats

}