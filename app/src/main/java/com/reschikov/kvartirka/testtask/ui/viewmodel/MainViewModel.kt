package com.reschikov.kvartirka.testtask.ui.viewmodel

import androidx.lifecycle.*
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.ui.Updateable
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private var derivable: Derivable?)
    : ViewModel(), Updateable {

    private val nameCityLiveData = MutableLiveData<String>()
    private val flatsLiveData = MutableLiveData<List<Flat>>()
    private val flatLiveData = MutableLiveData<Flat>()
    private val errorLiveData = MutableLiveData<Throwable?>()
    private val isVisibleProgressLiveData = MutableLiveData<Boolean>()
    private val observerResult by lazy {
        Observer<Triple<String?, List<Flat>?, Throwable?>> {
            it.first?.let {name -> nameCityLiveData.postValue(name) }
            it.second?.let {list -> flatsLiveData.postValue(list) }
            errorLiveData.postValue(it.third)
        }
    }
    private val observerProgress by lazy {
        Observer<Boolean> { isVisibleProgressLiveData.value = it  }
    }
    init {
        derivable?.isProgress()?.observeForever(observerProgress)
        derivable?.getResult()?.observeForever(observerResult)
    }

    override fun getNameCity(): LiveData<String> = nameCityLiveData
    override fun getFlats(): LiveData<List<Flat>> = flatsLiveData
    override fun getFlat(): LiveData<Flat> = flatLiveData
    override fun getError(): LiveData<Throwable?> = errorLiveData
    override fun isVisibleProgress(): LiveData<Boolean> = isVisibleProgressLiveData

    override fun getListOfAbs(widthPx: Int, heightPx: Int, isLocal: Boolean) {
        viewModelScope.launch {
            derivable?.requestListOfAds(widthPx, heightPx, isLocal)
        }
    }

    override fun toChoose(position : Int) {
        flatLiveData.postValue(flatsLiveData.value?.let { it[position] })
    }

    override fun onCleared() {
        super.onCleared()
        derivable?.getResult()?.removeObserver(observerResult)
        derivable?.isProgress()?.removeObserver(observerProgress)
        derivable?.terminate()
        derivable = null
    }
}