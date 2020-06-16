package com.reschikov.kvartirka.testtask.data.network.requestprovider

import android.os.Build
import com.reschikov.kvartirka.testtask.BuildConfig
import com.reschikov.kvartirka.testtask.data.Requestable
import com.reschikov.kvartirka.testtask.data.network.model.ReplyCities
import com.reschikov.kvartirka.testtask.data.network.model.ReplyFlats
import com.reschikov.kvartirka.testtask.data.network.request.ApiBetaKvartirkaPro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val TEST = "test"
private const val ANDROID ="android"

@Singleton
class KvartirkaProvider @Inject constructor(private val request: ApiBetaKvartirkaPro)  : Requestable{

    private val version = ANDROID + "-" + BuildConfig.VERSION_CODE
    private val numberOS = Build.VERSION.SDK_INT.toString()

    @Throws
    override suspend fun getCurrentCity(lat: Double, lng: Double): ReplyCities {
        return suspendCoroutine{continuation ->
            request.getCities(TEST, TEST, numberOS, version, false, lat, lng)
                .enqueue(getCallBack<ReplyCities>(continuation))
        }
    }

    @Throws
    override suspend fun getListFlats(widthPx : Int, heightPx : Int, pointLat: Double, pointLng: Double, cityId: Int): ReplyFlats {
        return suspendCoroutine{continuation ->
            request.getFlats(TEST, TEST, numberOS, version, widthPx, heightPx, pointLng, pointLat, cityId)
                .enqueue(getCallBack<ReplyFlats>(continuation))
        }
    }

    private fun <T> getCallBack(continuation: Continuation<T>): Callback<T> {
        return object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful){
                    response.body()?.let { continuation.resume(it) }
                } else {
                    response.errorBody()?.let { continuation.resumeWithException(Throwable(it.string())) }
                }
            }
        }
    }
}