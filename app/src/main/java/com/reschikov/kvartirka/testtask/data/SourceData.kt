package com.reschikov.kvartirka.testtask.data

import androidx.paging.PagingSource
import com.reschikov.kvartirka.testtask.PAGE_SIZE
import com.reschikov.kvartirka.testtask.domain.Ad
import com.reschikov.kvartirka.testtask.domain.Meta
import com.reschikov.kvartirka.testtask.domain.Reply
import com.reschikov.kvartirka.testtask.domain.Request
import com.reschikov.kvartirka.testtask.ui.viewmodel.Derivable
import com.reschikov.kvartirka.testtask.ui.viewmodel.Dispatchable

private const val OFFSET_START = 0
private const val NEAREST = 0

class SourceData(private val dispatchable: Dispatchable) : PagingSource<Meta, Ad>() {

    private val beginning = Meta(OFFSET_START, NEAREST, PAGE_SIZE)
    private lateinit var derivable : Derivable
    private lateinit var initRequest : Triple<Int, Int, Boolean>
    private var request : Request? = null

    override suspend fun load(params: LoadParams<Meta>) : LoadResult<Meta, Ad> {
        initRequest = dispatchable.getInitRequest() ?: return takeEmptyList()
        derivable = dispatchable.getDerivable() ?: return takeEmptyList()
        try {
            if (params is LoadParams.Prepend<Meta>) return prepend(params)
            if (params is LoadParams.Append<Meta>) return append(params)
            return refresh(params)
        } catch (e: Throwable) {
            return LoadResult.Error(e)
        } finally {
            dispatchable.setVisibleProgress(false)
        }
    }

    private suspend fun refresh(params: LoadParams<Meta>) : LoadResult<Meta, Ad>{
        dispatchable.setVisibleProgress(true)
        val position = params.key ?: beginning
        val reply  = request?.let { derivable.toRequest(it, position) } ?: derivable.initialRequest(initRequest , position)
        request = Request(initRequest.first, initRequest.second, reply.point, reply.city)
        return reply.run {
            dispatchable.setNameCity(city.name)
            val prevKey = if (position.offset <= OFFSET_START) null else position
            val nextKey = if (flats.isEmpty()) null else meta
            LoadResult.Page(flats, prevKey, nextKey)
        }
    }

    private suspend fun append(params : LoadParams.Append<Meta>) : LoadResult<Meta, Ad>{
        return request?.let {
            derivable.toRequest(it , params.key).run {
                val prevOffset = meta.offset - flats.size
                val prevKey = if (prevOffset < OFFSET_START) null else Meta(prevOffset, meta.nearest, flats.size)
                val nextKey = if (flats.isEmpty()) null else meta
                LoadResult.Page(flats, prevKey, nextKey)
            }
        } ?: takeEmptyList()
    }

    private suspend fun prepend(params : LoadParams.Prepend<Meta>) : LoadResult<Meta, Ad>{
        return  request?.let {
            val prevOffset = params.key.offset - params.key.limit
            val position: Meta =  if (prevOffset <= OFFSET_START) beginning else Meta(prevOffset, params.key.nearest, params.key.limit)
            derivable.toRequest(it, position).run {
                val prevKey = if (position.offset <= OFFSET_START) null else position
                val nextKey =  meta
                LoadResult.Page(flats, prevKey, nextKey)
            }
        } ?: takeEmptyList()
    }

    private fun takeEmptyList() : LoadResult.Page<Meta, Ad> {
        invalidate()
        return LoadResult.Page(emptyList(), null, null)
    }

    private suspend fun Derivable.initialRequest(initRequest : Triple<Int, Int,Boolean>, meta: Meta) : Reply{
        return  requestListOfAds(initRequest.first, initRequest.second, initRequest.third, meta)
    }

    private suspend fun Derivable.toRequest(request: Request, meta: Meta) : Reply {
        return  requestListOfAds(request, meta)
    }
}