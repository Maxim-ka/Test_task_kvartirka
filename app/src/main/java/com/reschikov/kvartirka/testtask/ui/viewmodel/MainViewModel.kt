package com.reschikov.kvartirka.testtask.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.reschikov.kvartirka.testtask.START_PAGE_SIZE
import com.reschikov.kvartirka.testtask.data.SourceData
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.domain.Request
import com.reschikov.kvartirka.testtask.ui.Updateable
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

private const val INITIAL_LOADE_SIZE = 5

class MainViewModel @Inject constructor(private var derivable: Derivable?)
    : ViewModel(), Updateable, Dispatchable {

    private val nameCityLiveData = MutableLiveData<String>()
    private val flatLiveData = MutableLiveData<Flat>()
    private val errorLiveData = MutableLiveData<Throwable?>()
    private val isVisibleProgressLiveData = MutableLiveData<Boolean>()
    private val observerStataNet by lazy {
        Observer<String?> {
            it?.let { errorLiveData.postValue(Exception(it)) } ?: run {

            }
        }
    }
    private val observerProgress by lazy {
        Observer<Boolean> { isVisibleProgressLiveData.value = it  }
    }
    private lateinit var flatsLiveData : LiveData<PagingData<Flat>>
    private lateinit var request: Request

    init {
        derivable?.isProgress()?.observeForever(observerProgress)
        derivable?.getStateNet()?.observeForever(observerStataNet)
    }

    override fun getNameCity(): LiveData<String> = nameCityLiveData
    override fun getFlats(): LiveData<PagingData<Flat>> = flatsLiveData
    override fun getFlat(): LiveData<Flat> = flatLiveData
    override fun getError(): LiveData<Throwable?> = errorLiveData
    override fun isVisibleProgress(): LiveData<Boolean> = isVisibleProgressLiveData

    override fun getListOfAbs(widthPx: Int, heightPx: Int, isLocal: Boolean) {
        val init = Triple(widthPx, heightPx, isLocal)
        val config = PagingConfig(START_PAGE_SIZE, INITIAL_LOADE_SIZE, false)
        flatsLiveData = Pager(config){SourceData(derivable!!, init)}.liveData.cachedIn(viewModelScope)
    }

    override fun toChoose(position : Int) {
//        flatLiveData.postValue(flatsLiveData.value?.let { it[position] })
    }

    override fun getDerivable(): Derivable = derivable!!
    override fun setNameCity(name: String) {
        nameCityLiveData.postValue(name)
    }

    override fun setError(err: Throwable?) {
        errorLiveData.postValue(err)
    }

    override fun saveRequest(request: Request) {
        this.request = request
    }

    override fun getRequest(): Request = request

    override fun onCleared() {
        super.onCleared()
        derivable?.getStateNet()?.removeObserver(observerStataNet)
        derivable?.isProgress()?.removeObserver(observerProgress)
        derivable?.terminate()
        derivable = null
    }
}