package com.example.in2000_team33.ui

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerItem(private val tittel: String, private val posisjon: LatLng, val id: Int): ClusterItem {

    override fun getPosition(): LatLng {
        return posisjon
    }

    override fun getTitle(): String {
        return tittel
    }

    override fun getSnippet(): String {
        return ""
    }
}