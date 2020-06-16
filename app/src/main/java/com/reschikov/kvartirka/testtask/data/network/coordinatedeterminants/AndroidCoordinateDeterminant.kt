package com.reschikov.kvartirka.testtask.data.network.coordinatedeterminants

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.domain.NoPermissionException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val DISTANCE = 1.0f

class AndroidCoordinateDeterminant(private var context: Context) : BaseCoordinateDeterminant(){

    private var lm: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val provider by lazy {
        lm.getBestProvider(Criteria().apply { accuracy = Criteria.ACCURACY_MEDIUM },
            true) ?: LocationManager.NETWORK_PROVIDER
    }
    private val strNoNetwork : String by lazy { context.getString(R.string.err_no_network) }
    private var locationListener: LocationListener? = null

    override suspend fun determineCoordinates(): Location  {
        return suspendCancellableCoroutine { continuation ->
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                continuation.resumeWithException(NoPermissionException())
            } else {
                val prov = this.provider
                locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let {locate ->
                            if (locate.accuracy <= setAccuracy) {
                                removeListener()
                                continuation.resume(locate)
                            } else setPeriod += setPeriod
                        }
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {
                        if (prov == provider) {
                            removeListener()
                            continuation.resumeWithException(Exception(strNoNetwork))
                        }
                    }
                }

                locationListener?.let {listener ->
                    lm.requestLocationUpdates(provider, setPeriod, DISTANCE, listener, Looper.getMainLooper())
                }
            }
        }
    }

    override fun removeListener() {
        locationListener?.let {
            lm.removeUpdates(it)
            locationListener = null
        }
    }
}