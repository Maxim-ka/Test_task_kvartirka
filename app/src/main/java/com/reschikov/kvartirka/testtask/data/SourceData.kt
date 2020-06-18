package com.reschikov.kvartirka.testtask.data

import android.util.Log
import androidx.paging.PagingSource
import com.reschikov.kvartirka.testtask.START_PAGE_SIZE
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.domain.Meta
import com.reschikov.kvartirka.testtask.domain.Reply
import com.reschikov.kvartirka.testtask.domain.Request
import com.reschikov.kvartirka.testtask.ui.viewmodel.Derivable
import com.reschikov.kvartirka.testtask.ui.viewmodel.Dispatchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val OFFSET_START = 0
private const val NEAREST = 0

class SourceData(private val derivable: Derivable,
                 private val initRequest : Triple<Int, Int,Boolean>)
    : PagingSource<Meta, Flat>() {

    private val beginning = Meta(OFFSET_START, NEAREST, START_PAGE_SIZE)

    override suspend fun load(params: LoadParams<Meta>): LoadResult<Meta, Flat> {
        val position = params.key ?: beginning
        val pair : Pair<Reply?, Throwable?>
//        if (params.key == null) {
            pair = initialRequest(position)
//            pair.first?.let { dispatchable.saveRequest(Request(initRequest.first, initRequest.second, it.city)) }
//        } else {
////            val request = dispatchable.getRequest()
////            pair = toRequest(request, position)
////            dispatchable.saveRequest(request)
//        }
        return getList(pair)
    }

    private fun getList(pair : Pair<Reply?, Throwable?>) : LoadResult.Page<Meta, Flat> {
        return pair.run {
//            dispatchable.setError(second)
            first?.let {
//                dispatchable.setNameCity(it.city.name)
                val prevKey = if (it.meta.offset == 0) null else it.meta
                val nextKey = if (it.flats.isEmpty()) null else it.meta
                Log.i("TAG first", (it.flats.size).toString())
                Log.i("TAG prevKey", (prevKey).toString())
                Log.i("TAG nextKey", (nextKey).toString())
                LoadResult.Page(it.flats, prevKey, nextKey)
            } ?: run {
                Log.i("TAG getList", "emptyList(), null, null")
                LoadResult.Page<Meta, Flat>(emptyList(), null, null)
            }
        }
    }

    private suspend fun initialRequest(position: Meta) : Pair<Reply?, Throwable?>{
        return derivable.requestListOfAds(initRequest.first, initRequest.second, initRequest.third, position)
    }

    private suspend fun toRequest(request: Request, position: Meta) : Pair<Reply?, Throwable?> {
        return withContext(Dispatchers.IO) {
            derivable.requestListOfAds(request, position)
        }
    }
}