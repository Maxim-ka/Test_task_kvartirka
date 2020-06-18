package com.reschikov.kvartirka.testtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.domain.Meta
import com.reschikov.kvartirka.testtask.domain.Reply
import com.reschikov.kvartirka.testtask.domain.Request
import com.reschikov.kvartirka.testtask.ui.viewmodel.Derivable
import javax.inject.Inject
import javax.inject.Singleton

private const val LAT_MOSCOW = 55.753960
private const val LNG_MOSCOW = 37.620393

@Singleton
class Repository @Inject constructor(private val checkNetWork: LiveData<String?>,
                                     private val definableCoordinates: DefinableCoordinates,
                                     private val requestable: Requestable,
                                     private val transformable: Transformable)
    : Derivable {

    private val netWorkLiveData = MutableLiveData<String?>()
    private val isProgressLiveData = MutableLiveData<Boolean>()
    private val observerNetWork by lazy {
        Observer<String?> { netWorkLiveData.postValue(it) }
    }

    init {
        checkNetWork.observeForever(observerNetWork)
    }

    override fun getStateNet(): LiveData<String?> = netWorkLiveData
    override fun isProgress(): LiveData<Boolean> = isProgressLiveData

    override suspend fun requestListOfAds(widthPx : Int, heightPx : Int, isLocal: Boolean, meta: Meta): Pair<Reply?, Throwable?> {
        isProgressLiveData.postValue(true)
        try {
            if (isLocal) {
                definableCoordinates.getCoordinates()?.let {location ->
                    getListFlats(widthPx, heightPx, location.latitude, location.longitude, meta)?.let {
                        return Pair(it, null)
                    }
                }
            }
            return Pair(getListMoscowFlats(widthPx, heightPx, meta), null)
        } catch (e: Exception) {
            return Pair(null, e)
        } finally {
            isProgressLiveData.postValue(false)
        }
    }

    override suspend fun requestListOfAds(request: Request, meta: Meta): Pair<Reply?, Throwable?> {
        isProgressLiveData.postValue(true)
        return try {
            request.run {
                val replyFlats = requestable.getListFlats(width, height, city.coordinates.lat, city.coordinates.lon, city.id, meta)
                Pair(transformable.transformToReply(city, replyFlats), null)
            }
        } catch (e: Exception) {
            Pair(null, e)
        } finally {
            isProgressLiveData.postValue(false)
        }
    }

    @Throws
    private suspend fun getListFlats(widthPx : Int, heightPx : Int, lat : Double, lng : Double, meta: Meta) :
            Reply? {
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
    }
}