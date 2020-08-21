package com.reschikov.kvartirka.testtask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.reschikov.kvartirka.testtask.LAT_MOSCOW
import com.reschikov.kvartirka.testtask.LNG_MOSCOW
import com.reschikov.kvartirka.testtask.PAGE_SIZE
import com.reschikov.kvartirka.testtask.domain.enteries.Point
import com.reschikov.kvartirka.testtask.domain.enteries.*
import com.reschikov.kvartirka.testtask.presentation.ui.viewmodel.Derivable

private const val OFFSET_START = 0
private const val NEAREST = 0

class SourceData(private val initRequest : Pair<SizePx, Boolean>,
                private var derivable: Derivable?) : PagingSource<Meta, Ad>() {

    private val nameCityLiveData = MutableLiveData<String>()
    private val beginning = Meta(OFFSET_START, NEAREST, PAGE_SIZE)
    private val point = if (initRequest.second) null else Point(
        LAT_MOSCOW,
        LNG_MOSCOW
    )
    private lateinit var requested : Derivable
    private lateinit var request : Request

    fun getNameCity(): LiveData<String> = nameCityLiveData
    fun clear() { derivable = null }

    override suspend fun load(params: LoadParams<Meta>) : LoadResult<Meta, Ad> {
        requested = derivable ?: return takeEmptyList()
        try {
            if (params is LoadParams.Prepend<Meta>) return prepend(params)
            if (params is LoadParams.Append<Meta>) return append(params)
            return refresh(params)
        } catch (e: Throwable) {
            return LoadResult.Error(e)
        }
    }

    private suspend fun refresh(params: LoadParams<Meta>) : LoadResult<Meta, Ad>{
        val position = params.key ?: beginning
        request = Request(initRequest.first, point, null, position)
        return toRequest().run {
            addRequest(this)
            val prevKey = if (position.offset <= OFFSET_START) null else position
            val nextKey = if (flats.isEmpty()) null else meta
            LoadResult.Page(flats, prevKey, nextKey)
        }
    }

    private suspend fun append(params : LoadParams.Append<Meta>) : LoadResult<Meta, Ad>{
        request.meta = params.key
        return toRequest().run {
            val prevOffset = meta.offset - flats.size
            val prevKey = if (prevOffset < OFFSET_START) null else Meta(prevOffset, meta.nearest, flats.size)
            val nextKey = if (flats.isEmpty()) null else meta
            LoadResult.Page(flats, prevKey, nextKey)
        }
    }

    private suspend fun prepend(params : LoadParams.Prepend<Meta>) : LoadResult<Meta, Ad>{
        val prevOffset = params.key.offset - params.key.limit
        val position: Meta =  if (prevOffset <= OFFSET_START) beginning else Meta(prevOffset, params.key.nearest, params.key.limit)
        request.meta = position
        return toRequest().run {
            val prevKey = if (position.offset <= OFFSET_START) null else position
            val nextKey =  meta
            LoadResult.Page(flats, prevKey, nextKey)
        }
    }

    private fun takeEmptyList() : LoadResult.Page<Meta, Ad> {
        return LoadResult.Page(emptyList(), null, null)
    }

    private suspend fun toRequest() : Reply = requested.requestListOfAds(request)

    private fun addRequest(reply: Reply) {
        request.apply { point ?: run { point = Point(reply.point.lat, reply.point.lon) }
            city ?: run {
                city = reply.city
                nameCityLiveData.postValue(reply.city.name)
            }
        }
    }
}