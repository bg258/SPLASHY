package com.example.in2000_team33.ui

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.in2000_team33.api.BadestedRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HjemViewModel(application: Application) : AndroidViewModel(application) {
    private var kameraPosisjon: CameraPosition? = null
    private val posisjon = PosisjonLiveData(application)
    private val markorer = MutableLiveData<List<MarkerItem>>()
    lateinit var klikkPosisjon: LatLng

    var visTips = true

    private val repo = BadestedRepository(application)

    val badesteder = repo.hentBadesteder().asLiveData()
    val favorittSteder = repo.hentFavorittSteder().asLiveData()

    fun hentMarkorer(): LiveData<List<MarkerItem>> {
        return markorer
    }

    fun hentKameraPosisjon(): CameraPosition? {
        return kameraPosisjon
    }

    fun settKameraPosisjon(posisjon: CameraPosition) {
        kameraPosisjon = posisjon
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun hentPosisjon(): PosisjonLiveData{
        return posisjon
    }

    fun oppdaterSok(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultat = repo.badestedSok(query)
            for (badested in resultat)
                Log.d("søkeresultat", badested.toString())
        }
    }

    fun oppdaterBadeforhold() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.oppdaterForecast()
            repo.oppdaterSoloppgang()
            repo.oppdaterObservasjoner()
        }
    }

    fun oppdaterMarkorer() {
        CoroutineScope(Dispatchers.IO).launch {
            val badestedEntities = repo.hentBadestedListe()
            markorer.postValue(badestedEntities.map { MarkerItem(it.navn, LatLng(it.lat, it.lon),it.id) })
        }
    }

    fun oppdaterAvstand(posisjon: LatLng) {
       CoroutineScope(Dispatchers.IO).launch {
          repo.oppdaterAvstand(posisjon)
       }
    }

    init {
        oppdaterBadeforhold()
        oppdaterMarkorer()
    }
}