package com.reschikov.kvartirka.testtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.reschikov.kvartirka.testtask.domain.Meta
import com.reschikov.kvartirka.testtask.domain.Reply
import com.reschikov.kvartirka.testtask.domain.Request
import com.reschikov.kvartirka.testtask.ui.viewmodel.Derivable
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

private const val LAT_MOSCOW = 55.753960
private const val LNG_MOSCOW = 37.620393

@Singleton
class Repository @Inject constructor(private val checkNetWork: LiveData<String?>,
                                     private val definableCoordinates: DefinableCoordinates,
                                     private val requestable: Requestable,
                                     private val transformable: Transformable)
    : Derivable {

    private val observerNetWork by lazy {
        Observer<String?> { it?.let{ coroutineRepository.cancel(CancellationException(it)) }}
    }
    private lateinit var coroutineRepository : CoroutineContext

    init {
        checkNetWork.observeForever(observerNetWork)
    }

    @Throws
    override suspend fun requestListOfAds(widthPx : Int, heightPx : Int, isLocal: Boolean, meta: Meta): Reply {
        coroutineRepository = Dispatchers.IO + Job()
        return withContext(coroutineRepository) {
            ensureActive()
            if (isLocal) {
                definableCoordinates.getCoordinates()?.let { location ->
                    ensureActive()
                    getListFlats(widthPx, heightPx, location.latitude, location.longitude, meta)?.let {
                        return@withContext it
                    }
                }
            }
            ensureActive()
            getListMoscowFlats(widthPx, heightPx, meta)
        }
    }

    @Throws
    override suspend fun requestListOfAds(request: Request, meta: Meta): Reply {
        coroutineRepository = Dispatchers.IO + Job()
        return withContext(coroutineRepository){
            request.run {
                ensureActive()
                val replyFlats = requestable.getListFlats(width, height, point.lat, point.lon, city.id, meta)
                transformable.transformToReply(city, replyFlats)
            }
        }
    }

    @Throws
    private suspend fun getListFlats(widthPx : Int, heightPx : Int, lat : Double, lng : Double, meta: Meta)
            : Reply? {
        val cities = requestable.getCurrentCity(lat, lng).cities
        if (cities.isEmpty()) return null
        val reply = requestable.getListFlats(widthPx, heightPx, lat, lng, cities.first().id, meta)
        val list = reply.flats
        if (list.isEmpty()) return null
        return transformable.transformToReply(cities.first(), reply)
    }

    private suspend fun getListMoscowFlats(widthPx : Int, heightPx : Int, meta: Meta)
            : Reply {
        val city = requestable.getCurrentCity(LAT_MOSCOW, LNG_MOSCOW).cities.first()
        val reply = requestable.getListFlats(widthPx, heightPx, LAT_MOSCOW, LNG_MOSCOW, city.id, meta)
        return transformable.transformToReply(city, reply)
    }

    override fun terminate() {
        checkNetWork.removeObserver(observerNetWork)
        if (coroutineRepository.isActive) coroutineRepository.cancel()
    }
}