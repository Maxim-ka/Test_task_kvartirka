package com.reschikov.kvartirka.testtask.data.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckNetWork @Inject constructor(private val context: Context) : LiveData<String?>() {

    private val connectivity : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val strNoNetWork : String = context.getString(com.reschikov.kvartirka.testtask.R.string.err_no_network)
    private var receiver : Receiver? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private var networkCallback : ConnectivityManager.NetworkCallback? = null

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            receiver = Receiver()
        } else {
            networkCallback = object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(null)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(strNoNetWork)
                }
            }
        }
    }

    override fun onActive() {
        super.onActive()
        startOff()
    }

    private fun startOff(){
        getCurrentStateNet()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) receiver?.let { context.registerReceiver(it, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)) }
        else networkCallback?.let { connectivity.registerNetworkCallback(createNetworkRequest(), it) }
    }

    override fun onInactive() {
        super.onInactive()
        complete()
    }

    private fun complete(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) receiver?.let { context.unregisterReceiver(it) }
        else networkCallback?.let { connectivity.unregisterNetworkCallback(it) }
    }

    private fun getCurrentStateNet() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (connectivity.activeNetwork != null) postValue(null)
            else  postValue(strNoNetWork)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (connectivity.isDefaultNetworkActive)  postValue(null)
            else  postValue(strNoNetWork)
        } else {
            val networkInfo = connectivity.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnectedOrConnecting)postValue(null)
            else  postValue(strNoNetWork)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNetworkRequest() : NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            .build()
    }

    inner class Receiver : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION){
                    getCurrentStateNet()
                }
            }
        }
    }
}