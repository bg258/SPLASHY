package com.example.in2000_team33.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastMapAPI {
    @GET("weatherapi/oceanforecast/2.0/complete")
    suspend fun fetchForecastOcean(@Query("lat") lat: String, @Query("lon") lon: String) : OceanForecastDto
    @GET("weatherapi/locationforecast/2.0/compact")
    suspend fun fetchLocationForecast(@Query("altitude") altitude: String, @Query("lat") lat: String, @Query("lon") lon: String) : LocationForecastDto
}

// ocean-forecast 2.0-beta
data class OceanForecastDto(val properties: PropertiesOcean)

data class PropertiesOcean(val timeseries: List<TimeSeriesOcean>)

data class TimeSeriesOcean(val data: DataOcean)

data class DataOcean(val instant: InstantOcean)

data class InstantOcean(val details: DetailsOcean)

data class DetailsOcean(val sea_water_temperature: String)


// location-forecast
data class LocationForecastDto(val properties: Properties)

data class Properties(val timeseries: List<TimeSeries>)

data class TimeSeries(val data: Data, val time: String)

data class Data(val instant: Instant, val next_6_hours: Next6Hour)

data class Instant(val details: Details)

data class Details(val air_temperature: String, val wind_speed: String)

data class Next6Hour(val summary: Summary)

data class Summary(val symbol_code: String)