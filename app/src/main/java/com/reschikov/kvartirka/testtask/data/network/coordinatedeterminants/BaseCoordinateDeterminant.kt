package com.reschikov.kvartirka.testtask.data.network.coordinatedeterminants

import android.location.Location
import com.reschikov.kvartirka.testtask.data.DefinableCoordinates
import kotlinx.coroutines.*

private const val INTERVAL_UPDATE = 2_000L
private const val MIN_ACCURACY = 200.0f
private const val SEARCH_TIME = 10_000L

abstract class BaseCoordinateDeterminant :
    DefinableCoordinates {

    protected var setPeriod = INTERVAL_UPDATE
    protected var setAccuracy = MIN_ACCURACY

    abstract suspend fun determineCoordinates(): Location

    @Throws
    override suspend fun getCoordinates() : Location? {
        return try {
                 withTimeoutOrNull(SEARCH_TIME){ determineCoordinates() }
            } finally {
                removeListener()
            }
    }

    abstract fun removeListener()
}