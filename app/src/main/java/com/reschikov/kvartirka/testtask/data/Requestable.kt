package com.reschikov.kvartirka.testtask.data

import com.reschikov.kvartirka.testtask.data.network.model.ReplyCities
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import com.reschikov.kvartirka.testtask.domain.Meta

interface Requestable {

    suspend fun getCurrentCity(lat : Double, lng : Double) : ReplyCities
    suspend fun getListFlats(widthPx: Int, heightPx: Int, pointLat: Double, pointLng: Double, cityId: Int, meta : Meta) : ReplyFlats

}