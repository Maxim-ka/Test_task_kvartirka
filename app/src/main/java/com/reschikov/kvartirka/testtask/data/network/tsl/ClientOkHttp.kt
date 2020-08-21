package com.reschikov.kvartirka.testtask.data.network.tsl

import android.content.Context
import com.reschikov.kvartirka.testtask.data.network.requestprovider.ActivateableTLS
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.util.*
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

private const val MAX_SIZE= 50L * 1024L * 1024L

@Singleton
class ClientOkHttp(private val context: Context) : ActivateableTLS {

    private val trustManager = getX509TrustManager()

    override fun getClient() : OkHttpClient = OkHttpClient
        .Builder()
        .sslSocketFactory(createTLSSocketFactory(), trustManager)
        .cache(Cache(context.cacheDir, MAX_SIZE))
        .build()

    override fun getContext() : Context = context

    @Throws(IllegalStateException::class)
    private fun getX509TrustManager() : X509TrustManager {
        val trustManagerFactory = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm())
            .apply { init(null as KeyStore?) }
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers.first() !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers))
        }
        return  trustManagers.first() as X509TrustManager
    }

    private fun createTLSSocketFactory() : TLSSocketFactory {
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf (trustManager), null)
        return TLSSocketFactory(sslContext.socketFactory)
    }
}