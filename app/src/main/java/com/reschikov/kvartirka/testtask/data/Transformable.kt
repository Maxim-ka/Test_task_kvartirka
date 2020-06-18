package com.reschikov.kvartirka.testtask.data

import com.reschikov.kvartirka.testtask.data.network.model.City
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import com.reschikov.kvartirka.testtask.domain.Reply

interface Transformable {
    fun transformToReply(city: City, replyFlats: ReplyFlats) : Reply
}