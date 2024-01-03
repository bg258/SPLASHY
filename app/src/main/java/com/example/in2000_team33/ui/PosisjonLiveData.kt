package com.example.in2000_team33.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class PosisjonLiveData(context: Context) : LiveData<Location>() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000)

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        //Forsøker å hente den siste lagrede posisjonen på enheten.
        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            if (location != null) {
                value = location
            }
        }

        //Starter nye posisjonsoppdateringer.
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onInactive() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //Håndter lokasjonsoppdateringer
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                value = location
            }
        }
    }
}
