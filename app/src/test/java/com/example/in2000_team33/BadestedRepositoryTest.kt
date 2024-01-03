package com.example.in2000_team33

import android.app.Application
import com.example.in2000_team33.api.BadestedRepository
import com.example.in2000_team33.db.BadestedEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BadestedRepositoryTest {

    @Mock
    private lateinit var application: Application

    private lateinit var repository: BadestedRepository


    private val eksempelBadesteder = listOf(
        BadestedEntity(1, "Huk", "Oslo", 0.0, 0.0, false, 100, null, null, null, null, null),
        BadestedEntity(2, "Huk", "Oslo", 0.0, 0.0, false, 100, null, null, null, null, "1619438373"),
        BadestedEntity(3, "Huk", "Oslo", 0.0, 0.0, false, 100, null, null, null, null, "1619442034")
    )

    @Before
    fun setup() {
        repository = BadestedRepository(application)
    }

    @Test
    fun oppdatert() {
        var tid: Long = 1619438434

        //Sjekk at sistlagret = null returnerer false
        assertEquals(false, repository.erOppdatert(eksempelBadesteder[0], tid))

        //Sjekk at tidspunk under 1 time
        assertEquals(true, repository.erOppdatert(eksempelBadesteder[1], tid))

        tid += 3600
        assertEquals(false, repository.erOppdatert(eksempelBadesteder[1], tid))
    }

}