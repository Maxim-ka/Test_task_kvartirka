package com.reschikov.kvartirka.testtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.reschikov.kvartirka.testtask.data.network.model.Flat
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
                                     private val requestable: Requestable)
    : Derivable, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    private val resultLiveData = MutableLiveData<Triple<String?, List<Flat>?, Throwable?>>()
    private val isProgressLiveData = MutableLiveData<Boolean>()
    private val observerNetWork by lazy {
        Observer<String?> {
            hasNoNet = it != null
            it?.let { resultLiveData.postValue(Triple(null, null, Exception(it))) } ?: run{
                request?.run {  requestListOfAds(first, second, third) }
            }
        }
    }
    private var hasNoNet = true
    private var request : Triple<Int, Int, Boolean>? = null

    init {
        checkNetWork.observeForever(observerNetWork)
    }

    override fun getResult(): LiveData<Triple<String?, List<Flat>?, Throwable?>> = resultLiveData
    override fun isProgress(): LiveData<Boolean> = isProgressLiveData

    override  fun requestListOfAds(widthPx : Int, heightPx : Int, isLocal : Boolean){
        request = Triple(widthPx, heightPx, isLocal)
        if (hasNoNet) return
        isProgressLiveData.value = true
        launch {
            try {
                if (isLocal) {
                    definableCoordinates.getCoordinates()?.let {location ->
                        getListFlats(widthPx, heightPx, location.latitude, location.longitude)?.let {
                            resultLiveData.postValue(it)
                            return@launch
                        }
                    }
                }
                resultLiveData.postValue(getListMoscowFlats(widthPx, heightPx))
            } catch (e: Exception) {
                if (!hasNoNet) request = null
                resultLiveData.postValue(Triple(null, null, e))
            } finally {
                isProgressLiveData.postValue(false)
            }
        }
    }

    @Throws
    private suspend fun getListFlats(widthPx : Int, heightPx : Int, lat : Double, lng : Double) :
            Triple<String?, List<Flat>?, Throwable?>? {
        val cities = requestable.getCurrentCity(lat, lng).cities
        if (cities.isEmpty()) return null
        val list = requestable.getListFlats(widthPx, heightPx, lat, lng, cities.first().id).flats
        if (list.isEmpty()) return null
        request = null
        return Triple(cities.first().name, list, null)
    }

    private suspend fun getListMoscowFlats(widthPx : Int, heightPx : Int)
            : Triple<String?, List<Flat>?, Throwable?>{
        val city = requestable.getCurrentCity(LAT_MOSCOW, LNG_MOSCOW).cities.first()
        val list = requestable.getListFlats(widthPx, heightPx, LAT_MOSCOW, LNG_MOSCOW, city.id).flats
        request = null
        return Triple(city.name, list, null)
    }

    override fun terminate() {
        checkNetWork.removeObserver(observerNetWork)
        coroutineContext.cancel()
    }
}