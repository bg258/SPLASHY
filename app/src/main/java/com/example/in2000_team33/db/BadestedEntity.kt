package com.example.in2000_team33.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BadestedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val navn: String,
    val sted: String,
    val lat: Double,
    val lon: Double,
    var favoritt: Boolean,
    var avstand: Int?,
    var badeforhold: List<BadestedForecast>?,
    val observert: Double?,
    val solnedgang: String?,
    val soloppgang: String?,
    val sistOppdatert: String?
)

data class AvstandsOppdatering(
    val id: Int,
    var avstand: Int?
)

data class ObservertTemperaturOppdatering(
    val id: Int,
    val observert: Double?
)

data class SunriseOppdatering(

    val id: Int,
    val solnedgang: String?,
    val soloppgang: String?
)

data class ForecastOppdatering(
    val id: Int,
    val badeforhold: List<BadestedForecast>,
    val sistOppdatert: String
)

data class BadestedForecast(
    val havTemperatur: Double?,
    val luftTemperatur: Double?,
    val weather: String?,
    val wind: String?,
    val time: String?
)

