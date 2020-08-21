package com.reschikov.kvartirka.testtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.reschikov.kvartirka.testtask.LAT_MOSCOW
import com.reschikov.kvartirka.testtask.LNG_MOSCOW
import com.reschikov.kvartirka.testtask.domain.enteries.City
import com.reschikov.kvartirka.testtask.domain.enteries.Point
import com.reschikov.kvartirka.testtask.domain.enteries.Meta
import com.reschikov.kvartirka.testtask.domain.enteries.Reply
import com.reschikov.kvartirka.testtask.domain.enteries.Request
import com.reschikov.kvartirka.testtask.domain.enteries.SizePx
import com.reschikov.kvartirka.testtask.presentation.ui.viewmodel.Derivable
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class Repository @Inject constructor(
    private val checkNetWork: LiveData<String?>,
    private val definableCoordinates: DefinableCoordinates,
    private val requestable: Requestable,
    private val transformable: Transformable) : Derivable {

    private val observerNetWork by lazy {
        Observer<String?> { it?.let{ coroutineRepository.cancel(CancellationException(it))
            } ?: run {
            if (!coroutineRepository.isActive) coroutineRepository = Dispatchers.IO + Job() }
        }
    }
    private var coroutineRepository : CoroutineContext = Dispatchers.IO + Job()

    init {
        checkNetWork.observeForever(observerNetWork)
    }

    @Throws
    override suspend fun requestListOfAds(request: Request): Reply {
        return withContext(coroutineRepository){
            ensureActive()
            request.run {
                point?.let {
                    val city = city ?: getCity(it)
                    getListFlats(sizePx, it, city, meta)
                } ?: run {
                    getLocation()?.let {
                        getListFlats(sizePx, it,  getCity(it), meta)
                    } ?: run{ getListMoscowFlats(sizePx, meta) }
                }
            }
        }
    }

    private suspend fun getLocation() : Point? {
        coroutineRepository.ensureActive()
        return definableCoordinates.getCoordinates()?.let {
            Point(it.latitude, it.longitude)
        }
    }

    private suspend fun getCity(point: Point) : City {
        coroutineRepository.ensureActive()
        val cities = requestable.getCurrentCity(point.lat, point.lon).cities
        if (cities.isEmpty()) return requestable.getCurrentCity(LAT_MOSCOW, LNG_MOSCOW).cities.first()
        return cities.first()
    }

    private suspend fun getListFlats(sizePx: SizePx, point: Point, city : City, meta: Meta) : Reply {
        coroutineRepository.ensureActive()
        val reply = requestable.getListFlats(sizePx, point, city.id, meta)
        if (reply.flats.isEmpty()) return Reply(emptyList(), point, city, meta)
        return transformable.transformToReply(city, reply)
    }

    private suspend fun getListMoscowFlats(sizePx: SizePx, meta: Meta) : Reply {
        val point = Point(LAT_MOSCOW, LNG_MOSCOW)
        coroutineRepository.ensureActive()
        val city = getCity(point)
        val reply = requestable.getListFlats(sizePx, point, city.id, meta)
        return transformable.transformToReply(city, reply)
    }

    override fun terminate() {
        if (coroutineRepository.isActive) coroutineRepository.cancel()
        checkNetWork.removeObserver(observerNetWork)
    }
}