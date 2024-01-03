package com.example.in2000_team33.util

import android.content.Context
import com.example.in2000_team33.db.AppDatabase
import com.example.in2000_team33.db.BadestedEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LagreBadested {
    companion object {
        fun leggTilBadested(badested: BadestedEntity, applicationContext: Context): Boolean {
            val db = AppDatabase.hentDatabase(applicationContext)
            val dao = db.badestedDao()

            if (validerBadested(badested.navn, badested.sted)) {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.lagre(badested)
                }
            } else {

                return false
            }

            return true
        }

        /**
         * Sjekker at informasjonen brukeren oppgir er gyldig.
         */
        fun validerBadested(navn: String, sted: String): Boolean {
            if (navn.isBlank() || sted.isBlank()) return false
            if (navn.length > 20 || sted.length > 20) return false;

            return true
        }
    }
}