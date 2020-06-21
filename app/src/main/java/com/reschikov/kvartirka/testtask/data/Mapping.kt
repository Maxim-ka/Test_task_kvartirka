package com.reschikov.kvartirka.testtask.data

import com.reschikov.kvartirka.testtask.data.network.model.City
import com.reschikov.kvartirka.testtask.data.network.model.Currency
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import com.reschikov.kvartirka.testtask.domain.Ad
import com.reschikov.kvartirka.testtask.domain.Reply
import javax.inject.Inject

class Mapping @Inject constructor() : Transformable{

    override fun transformToReply(city: City, replyFlats: ReplyFlats) : Reply{
        return Reply(city, createListAd(replyFlats.currency,replyFlats.flats), replyFlats.query.filter.point, replyFlats.query.meta)
    }

    private fun createListAd(currency: Currency, flats: List<Flat>) : List<Ad>{
        return flats.map { transformToAd(currency, it) }
    }

    private fun transformToAd(currency: Currency, flat: Flat) : Ad {
        return Ad(flat, currency.label)
    }
}