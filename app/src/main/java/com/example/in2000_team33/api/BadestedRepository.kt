package com.example.in2000_team33.api

import android.app.Application
import android.util.Log
import com.example.in2000_team33.db.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.NullPointerException
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BadestedRepository(application: Application) {
    private val db = AppDatabase.hentDatabase(application)
    private val badestedDao = db.badestedDao()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://in2000-apiproxy.ifi.uio.no/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: ForecastMapAPI = retrofit.create(
        ForecastMapAPI::class.java)

    val frostAPI = Retrofit.Builder()
        .baseUrl("http://havvarsel-frost.met.no/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(FrostApi::class.java)

    val sunriseApi = Retrofit.Builder()
        .baseUrl("https://in2000-apiproxy.ifi.uio.no/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(SunriseApi::class.java)

    /**
     * Henter badestedene i den lokale databasen.
     *
     * Returnerer en flowliste, som oppdaterer seg selv hver gang roomdataen endres.
     */
    fun hentBadesteder(): Flow<List<BadestedEntity>> {
        return badestedDao.hentBadesteder()
    }

    fun badestedSok(string: String): List<BadestedEntity> {
        return badestedDao.finnBadesteder("%$string%")
    }

    /**
     * Returnerer en liste over badestedene
     */
    fun hentBadestedListe(): List<BadestedEntity> {
        return badestedDao.hentBadestedListe()
    }

    /**
     * Oppdater værvarsling for hvert badested
     */
    suspend fun oppdaterForecast() {
        //Tidspunkt for oppdateringen
        val tid = System.currentTimeMillis() / 1000

        //Henter badesteder fra room, sortert etter avstand, slik at mest relevante steder oppdateres først.
        val badesteder = badestedDao.hentNaermeste()

        //Henter forecast for hvert badested.
        for (badested in badesteder) {
            if (erOppdatert(badested, tid))
                continue

            try {
                val result_location = service.fetchLocationForecast("0", badested.lat.toString(), badested.lon.toString())
                val result_ocean = service.fetchForecastOcean(badested.lat.toString(), badested.lon.toString())

                val forecasts = mutableListOf<BadestedForecast>()
                for (i in listOf(0, 6, 12, 24)) {
                    // current temperature
                    val temp_ocean = result_ocean.properties.timeseries[i].data.instant.details.sea_water_temperature
                    val temp_air = result_location.properties.timeseries[i].data.instant.details.air_temperature
                    val weather = result_location.properties.timeseries[i].data.next_6_hours.summary.symbol_code
                    val wind = result_location.properties.timeseries[i].data.instant.details.wind_speed
                    val time = result_location.properties.timeseries[i].time

                    forecasts.add(BadestedForecast(temp_ocean.toDouble(), temp_air.toDouble(), weather, wind, time))
                }

                badestedDao.oppdaterBadeforhold(ForecastOppdatering(badested.id, forecasts, tid.toString()))
            } catch (err: HttpException) {
                Log.e("Forecast", "Klarte ikke å hente forecast for $badested")
            } catch (err: IndexOutOfBoundsException) {
                Log.e("Forecast", "Klarte ikke å hente forecast for $badested")
            }
        }
    }

    /**
     * Oppdaterer soloppgang for hvert sted.
     */
    suspend fun oppdaterSoloppgang() {
        val dato = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
        val badesteder = badestedDao.hentNaermeste()

        val sunrise = hashMapOf<String, String>()
        val sunset = hashMapOf<String, String>()

        Log.i("Badestedrepository", "Oppdaterer soloppgang!")

        for (badested in badesteder) {
            //Hvis soloppgang allerede er oppdatert for dagen, ikke oppdater igjen.
            if (badested.soloppgang != null) {
                val tidspunkt = badesteder[0].soloppgang?.substringBefore("+")
                if (tidspunkt != null && LocalDateTime.parse(tidspunkt).dayOfMonth == LocalDateTime.now().dayOfMonth)
                    continue
            }

            try {
                //Hent solnedgang og soloppgang for hvert sted.
                if (sunrise[badested.sted] == null) {

                    //Henter soloppgang og solnedgang fra sunrise.
                    val sunriseResultat = sunriseApi.fetchSunrise(badested.lat.toString(), badested.lon.toString(), dato)

                    val soloppgang = sunriseResultat.location.time.first().sunrise.time
                    val solnedgang = sunriseResultat.location.time.first().sunset.time

                    sunrise[badested.sted] = soloppgang
                    sunset[badested.sted] = solnedgang
                }

                badestedDao.oppdaterSunrise(SunriseOppdatering(badested.id, sunset[badested.sted], sunrise[badested.sted]))
            } catch (err: HttpException) {
                Log.d("Forecast", "Klarte ikke å hente soloppgang for $badested")
            } catch (err: NullPointerException) {
                Log.d("Forecast", "Klarte ikke å hente soloppgang for $badested")
            }
        }
    }

    fun erOppdatert(badested: BadestedEntity, tid: Long): Boolean {
        badested.sistOppdatert?.let {
            if (tid - it.toLong() < 3600)
                return true
        }
        return false
    }

    /**
     * Oppdater observert temperatur
     */
    suspend fun oppdaterObservasjoner() {
        //Hent badesteder og temperaturobservasjon.
        val badesteder = badestedDao.hentBadestedListe()

        //Start og sluttidspunkt for observasjoner
        val startTid = ZonedDateTime.now().minusHours(12).format(DateTimeFormatter.ISO_INSTANT)
        val sluttTid = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)

        val result = frostAPI.fetchBadevannTemperature("$startTid/$sluttTid")

        //For hvert badested, sjekk om det finnes en observasjon med tilsvarende posisjon
        for(badested in badesteder) {
            result.data.tseries.find {
                it.header.extra.pos.lat.toDouble() == badested.lat &&
                it.header.extra.pos.lon.toDouble() == badested.lon
            }.let {
                //Oppdater room med den nye observasjonen.
                badestedDao.oppdaterObservertTemperatur(ObservertTemperaturOppdatering(badested.id,  it?.observations?.last()?.body?.value?.toDouble()))
            }

        }

    }


    /**
     * Oppdaterer avstanden til hvert badested
     */
    fun oppdaterAvstand(posisjon: LatLng) {
        val badesteder = badestedDao.hentBadestedListe()
        for (badested in badesteder) {
            val avstand = SphericalUtil.computeDistanceBetween(posisjon, LatLng(badested.lat, badested.lon)).toInt()
            badestedDao.oppdaterAvstand(AvstandsOppdatering(badested.id, avstand))
        }
    }
    /**
     * Returner liste med favoritt stedene til brukeren
     */

    fun hentFavorittSteder() : Flow<List<BadestedEntity>> {
        return badestedDao.hentFavoritter()
    }

    /**
     * Marekerer et badested som favorittsted
     */
    fun leggTilFavoritt(stedId: Int) {
        CoroutineScope(Dispatchers.IO).launch{ badestedDao.leggTilFavoritt(stedId) }
    }

    /**
     * fjern favoritt
     */
    fun fjernFavoritt(stedId: Int) {
        CoroutineScope(Dispatchers.IO).launch{ badestedDao.fjernFavoritt(stedId) }
    }

    fun hentBadested(placeId: Int) : BadestedEntity {
        return badestedDao.hentBadested(placeId)
    }
}