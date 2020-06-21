package com.reschikov.kvartirka.testtask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.reschikov.kvartirka.testtask.PAGE_SIZE
import com.reschikov.kvartirka.testtask.data.SourceData
import com.reschikov.kvartirka.testtask.domain.Ad
import com.reschikov.kvartirka.testtask.ui.Updateable
import javax.inject.Inject

private const val INITIAL_LOADE_SIZE = 10

class MainViewModel @Inject constructor(private var derivable: Derivable?)
    : ViewModel(), Updateable, Dispatchable {

    private val nameCityLiveData = MutableLiveData<String>()
    private val flatLiveData = MutableLiveData<Ad>()
    private val errorLiveData = MutableLiveData<Throwable?>()
    private val isVisibleProgressLiveData = MutableLiveData<Boolean>()
    private var flatsLiveData : LiveData<PagingData<Ad>>? = null
    private var initRequest :  Triple<Int, Int, Boolean>? = null

    override fun getNameCity(): LiveData<String> = nameCityLiveData
    override fun getFlats(): LiveData<PagingData<Ad>> = getLiveDataPagingData()
    override fun getFlat(): LiveData<Ad> = flatLiveData
    override fun setError(e: Throwable) {
        errorLiveData.postValue(e)
    }

    override fun getError(): LiveData<Throwable?> = errorLiveData
    override fun isVisibleProgress(): LiveData<Boolean> = isVisibleProgressLiveData

    override fun getListOfAbs(widthPx: Int, heightPx: Int, isLocal: Boolean) {
        initRequest = Triple(widthPx, heightPx, isLocal)
        flatsLiveData = createLiveDataPagedList()
    }

    private fun getLiveDataPagingData() : LiveData<PagingData<Ad>>{
        return flatsLiveData ?: createLiveDataPagedList().also {  flatsLiveData = it }
    }

     private fun createLiveDataPagedList() : LiveData<PagingData<Ad>>{
        val config = createPagedListConfig()
        return Pager(config){SourceData(this)}.liveData.cachedIn(viewModelScope)
    }

    private fun createPagedListConfig() : PagingConfig{
        return PagingConfig(PAGE_SIZE, INITIAL_LOADE_SIZE, false)
    }

    override fun toChoose(item : Ad) {
        flatLiveData.postValue(item)
    }

    override fun getDerivable(): Derivable? = derivable

    override fun setNameCity(name: String) {
        nameCityLiveData.postValue(name)
    }

    override fun getInitRequest(): Triple<Int, Int, Boolean>? = initRequest
    override fun setVisibleProgress(isVisible: Boolean) {
        isVisibleProgressLiveData.postValue(isVisible)
    }

    override fun onCleared() {
        super.onCleared()
        derivable?.terminate()
        derivable = null
    }
}