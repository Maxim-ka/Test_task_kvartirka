package com.reschikov.kvartirka.testtask.data.network.model

import android.content.Context
import com.reschikov.kvartirka.testtask.R
import java.lang.StringBuilder

private const val LINE_BREAK = "\n"

data class Prices(private val day : Int,
                  private val night : Int,
                  private val hour : Int){


    fun getPrice(context: Context, currency: String) : String{
        val sb = StringBuilder(context.getString(R.string.price)).append(LINE_BREAK)
        if (day != 0) sb.append(context.getString(R.string.day)).append(day).append(currency).append(LINE_BREAK)
        if (night != 0) sb.append(context.getString(R.string.night)).append(night).append(currency).append(LINE_BREAK)
        if (hour != 0) sb.append(context.getString(R.string.hour)).append(hour).append(currency)
        return sb.toString()
    }
}