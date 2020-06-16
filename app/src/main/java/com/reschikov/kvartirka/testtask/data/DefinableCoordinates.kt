package com.reschikov.kvartirka.testtask.data

import android.location.Location

interface DefinableCoordinates {
   suspend fun getCoordinates() : Location?
}