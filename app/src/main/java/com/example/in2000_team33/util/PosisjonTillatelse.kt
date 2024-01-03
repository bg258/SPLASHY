package com.example.in2000_team33.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

/**
 * Denne filen inneholder extensions som gjør det enklere
 * å sjekke instillinger relatert til henting av brukerens posisjon i fragmenter.
 */



/**
 * Sjekker posisjonstillatelse
 */
fun Fragment.sjekkPosisjonstillatelse(): Boolean {
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
}


/**
 * Sjekker om systeminstillingene er optimale for posisjonsforespørsler
 *
 * Kaller på callback med null hvis alt er i orden, eller med en exeption hvis instillingene ikke er optimale.
 */
fun Fragment.sjekkPosisjonsinstillinger(callback: (Exception?) -> Unit) {
    val locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    //Lager en forespørsel for å sjekke om systeminstillingene er optimale
    val settingsClient: SettingsClient = LocationServices.getSettingsClient(requireContext())
    val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(locationSettingsRequest.build())

    //Systeminstillingene er optimale
    task.addOnSuccessListener {
        callback(null)
    }

    //Systeminstillingene er ikke optimale
    task.addOnFailureListener { exception ->
        callback(exception)
    }
}
