package com.example.in2000_team33.api

import retrofit2.http.GET
import retrofit2.http.Query

private const val TIME_INTERVAL = "2020-01-01T00:00:00Z/2021-03-31T23:59:59Z" // tidsintervall for data, bør overskrives med dagens dato når appen er i bruk
private const val INCOBS = "true" // overskriver default verdi false, hvilket betyr at api-kallet henter observasjoner fra bøyene itillegg til metadata

interface FrostApi {
    @GET("api/v1/obs/badevann/get")
    suspend fun fetchBadevannTemperature(@Query("time") time: String = TIME_INTERVAL,
                                         @Query("incobs") incobs : String = INCOBS
    ) : BadevannDto
}


// Dataklasser i Havvarsel-Frost API
data class BadevannDto(val data : SunriseData)

data class SunriseData(val tseries: List<Tseries>)
data class Tseries(val header : Header, val observations: List<Observations>)

data class Header(val extra : Extra)
data class Extra(val name : String, val pos : Pos)
data class Pos(val lon : String, val lat : String)

data class Observations(val time: String, val body : Body)
data class Body(val value: String)

