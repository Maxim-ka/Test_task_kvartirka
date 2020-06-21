package com.reschikov.kvartirka.testtask

import android.app.Activity
import android.util.DisplayMetrics

const val KEY_POSITION = "key position"
const val KEY_PHOTOS = "key photos"
const val PAGE_SIZE = 40
const val REQUEST_GOOGLE_COORDINATE = 1
const val REQUEST_MY_PERMISSIONS_ACCESS_LOCATION = 10

private const val HALF = 0.5f

fun Activity.getSizeDisplay() : Pair<Int, Int>{
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val width = (displayMetrics.widthPixels * HALF).toInt()
    val height = (displayMetrics.heightPixels * HALF).toInt()
    return Pair(width, height)
}