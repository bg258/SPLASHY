package com.example.in2000_team33

import com.example.in2000_team33.util.LagreBadested
import org.junit.Test

import org.junit.Assert.*

class LagreBadestedTest {
    @Test
    fun testVerifisering() {

        //Sjekker at gyldige navn blir gottatt.
        assertEquals(true, LagreBadested.validerBadested("Huk",  "Oslo"))
        assertEquals(true, LagreBadested.validerBadested("Bygdøy sjøbad",  "Oslo"))

        //Sjekker at for lange navn ikke blir godtatt.
        assertEquals(false, LagreBadested.validerBadested("Navn som er for langt.", "Oslo"))

        //Sjekk at blanke navn ikke blir godtatt.
        assertEquals(false, LagreBadested.validerBadested("",  ""))
        assertEquals(false, LagreBadested.validerBadested(" ",  " "))
        assertEquals(false, LagreBadested.validerBadested("\n",  "\n"))
        assertEquals(false, LagreBadested.validerBadested("",  "\n"))
        assertEquals(false, LagreBadested.validerBadested("",  "Oslo"))
        assertEquals(false, LagreBadested.validerBadested("Huk",  ""))
    }
}