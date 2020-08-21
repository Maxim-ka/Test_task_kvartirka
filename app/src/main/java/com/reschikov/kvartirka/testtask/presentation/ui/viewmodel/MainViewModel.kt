package com.reschikov.kvartirka.testtask.presentation.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.reschikov.kvartirka.testtask.PAGE_SIZE
import com.reschikov.kvartirka.testtask.data.SourceData
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.domain.enteries.SizePx
import com.reschikov.kvartirka.testtask.presentation.ui.Updateable
import javax.inject.Inject

private const val INITIAL_LOADE_SIZE = 10

class MainViewModel @Inject constructor(private var derivable: Derivable?) : ViewModel(), Updateable{

    private val nameCityLiveData = MutableLiveData<String>()
    private val flatLiveData = MutableLiveData<Ad>()
    private val observerNameCity by lazy { Observer<String>{ nameCityLiveData.postValue(it) } }
    private lateinit var flatsLiveData : LiveData<PagingData<Ad>>
    private lateinit var sourceData: SourceData

    override fun getNameCity(): LiveData<String> = nameCityLiveData
    override fun getFlats(): LiveData<PagingData<Ad>> = flatsLiveData
    override fun getFlat(): LiveData<Ad> = flatLiveData
    override fun setRequestParameters(sizePx: SizePx, isLocal: Boolean) {
        flatsLiveData = createLiveDataPagedList(Pair(sizePx, isLocal))
    }

    private fun createLiveDataPagedList(initRequest : Pair<SizePx, Boolean>) : LiveData<PagingData<Ad>>{
        val config = createPagedListConfig()
        return Pager(config){ createSourceData(initRequest) }.liveData.cachedIn(viewModelScope)
    }

    private fun createPagedListConfig() : PagingConfig{
        return PagingConfig(PAGE_SIZE, INITIAL_LOADE_SIZE, false)
    }

    private fun createSourceData(initRequest : Pair<SizePx, Boolean>) : SourceData {
        if(this::sourceData.isInitialized) {
            sourceData.getNameCity().removeObserver(observerNameCity)
            sourceData.clear()
        }
        sourceData = SourceData(initRequest, derivable)
        sourceData.getNameCity().observeForever(observerNameCity)
        return sourceData
    }

    override fun toChoose(item : Ad) {
        flatLiveData.postValue(item)
    }

    override fun onCleared() {
        super.onCleared()
        derivable?.terminate()
        derivable = null
    }
}