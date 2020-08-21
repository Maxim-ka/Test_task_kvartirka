package com.reschikov.kvartirka.testtask.data

import android.content.Context
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.data.network.model.*
import com.reschikov.kvartirka.testtask.domain.enteries.City
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.domain.enteries.Reply
import java.lang.StringBuilder
import javax.inject.Inject

private const val LINE_BREAK = "\n"

class Mapping @Inject constructor(private val context: Context) : Transformable{

    override fun transformToReply(city: City, replyFlats: ReplyFlats) : Reply{
        return replyFlats.run {
            Reply(createListAd(currency.label, flats), query.filter.point, city, query.meta)
        }
    }

    private fun createListAd(currencyName: String, flats: List<Flat>) : List<Ad>{
        return flats.map { transformToAd(currencyName, it) }
    }

    private fun transformToAd(currencyName: String, flat: Flat) : Ad {
        return flat.run {
            Ad(flat.id, title, address, type, rooms, getPrice(prices, currencyName), photo.url, getUrlPhoto(photos))
        }
    }

    private fun getPrice(prices: Prices, currency: String) : String{
        return prices.run {
            StringBuilder(context.getString(R.string.price)).append(LINE_BREAK).apply {
                if (day != 0){
                    append(context.getString(R.string.day))
                        .append(day)
                        .append(currency)
                        .append(LINE_BREAK)
                }
                if (night != 0){
                    append(context.getString(R.string.night))
                        .append(night)
                        .append(currency)
                        .append(LINE_BREAK)
                }
                if (hour != 0) {
                    append(context.getString(R.string.hour)).append(hour).append(currency)
                }
            }
        }.toString()
    }

    private fun getUrlPhoto(photos : List<Photo>) : List<String>{
        return photos.map { photo -> photo.url }
    }
}