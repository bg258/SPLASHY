package com.example.in2000_team33.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    val gson = Gson()

    @TypeConverter
    fun strengTilListe(streng: String?): List<BadestedForecast>? {
        if (streng == null) return null

        val collectionType: Type = object : TypeToken<List<BadestedForecast?>?>() {}.type

        return gson.fromJson(streng, collectionType)
    }

    @TypeConverter
    fun listeTilStreng(liste: List<BadestedForecast>?): String? {
        if (liste == null) return null

        return gson.toJson(liste)
    }
}