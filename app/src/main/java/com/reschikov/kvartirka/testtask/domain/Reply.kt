package com.reschikov.kvartirka.testtask.domain

import com.reschikov.kvartirka.testtask.data.network.model.City
import com.reschikov.kvartirka.testtask.data.network.model.Currency
import com.reschikov.kvartirka.testtask.data.network.model.Flat

data class Reply(val city: City,
                 val currency: Currency,
                 val flats: List<Flat>,
                 val meta: Meta)