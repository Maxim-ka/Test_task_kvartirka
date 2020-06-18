package com.reschikov.kvartirka.testtask.data

import com.reschikov.kvartirka.testtask.data.network.model.City
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import com.reschikov.kvartirka.testtask.domain.Reply
import javax.inject.Inject

class Mapping @Inject constructor() : Transformable{

    override fun transformToReply(city: City, replyFlats: ReplyFlats) : Reply{
        return Reply(city, replyFlats.currency, replyFlats.flats, replyFlats.query.meta)
    }
}