package com.example.in2000_team33.api

import retrofit2.http.Query
import retrofit2.http.GET

private const val CURRENT_DATE : String = "2021-04-29"
private const val TIME_OFFSET_NORWAY : String = "+01:00"

interface SunriseApi {
    @GET("weatherapi/sunrise/2.0/.json")
    suspend fun fetchSunrise(@Query("lat") lat : String,
                             @Query("lon") lon : String,
                             @Query("date") date : String = CURRENT_DATE,
                             @Query("offset") offset: String = TIME_OFFSET_NORWAY
    ) : SunriseDto

}

// Dataklasser i Sunrise apiet
data class SunriseDto(val location : Location)
data class Location(val time : List<Time>)
data class Time(val sunrise : Sunrise, val sunset : Sunset)
data class Sunrise(val time : String)
data class Sunset(val time : String)