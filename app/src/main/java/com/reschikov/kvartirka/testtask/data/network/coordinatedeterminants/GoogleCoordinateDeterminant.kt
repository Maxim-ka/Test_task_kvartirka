package com.reschikov.kvartirka.testtask.data.network.coordinatedeterminants

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.reschikov.kvartirka.testtask.domain.enteries.NoPermissionException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleCoordinateDeterminant(private val context: Context) : BaseCoordinateDeterminant(){

    private val locationRequest = LocationRequest.create().apply {
        interval = setPeriod
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }
    private val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

    private val locationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var lcb: LocationCallback? = null
    private var  taskSetting: Task<LocationSettingsResponse>? = null

    override suspend fun determineCoordinates(): Location {
        return suspendCancellableCoroutine {continuation ->
            checkParameters()
            taskSetting?.addOnSuccessListener {
                if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    continuation.resumeWithException(NoPermissionException())
                } else {
                    lcb = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            locationResult?.let {result ->
                                val location = result.lastLocation
                                if (location.accuracy <= setAccuracy) {
                                    locationProviderClient.removeLocationUpdates(this)
                                    lcb = null
                                    continuation.resume(location)
                                } else {
                                    setAccuracy += setAccuracy
                                }
                            }
                        }
                    }
                    val task = locationProviderClient.requestLocationUpdates(locationRequest, lcb, null)

                    task.addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
                }
            }

            taskSetting?.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
    }

    private fun checkParameters(){
        taskSetting = LocationServices.getSettingsClient(context)
                .checkLocationSettings(builder.build())
    }

    override fun removeListener() {
        lcb?.let {
            locationProviderClient.removeLocationUpdates(it)
            lcb = null
        }
    }
}