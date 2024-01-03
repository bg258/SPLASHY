package com.example.in2000_team33

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.in2000_team33.db.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BadestedDaoTest {
    private lateinit var badestedDao: BadestedDao
    private lateinit var db: AppDatabase

    private val badesteder = listOf(
        BadestedEntity(0, "Bragernes strand", "Drammen", 59.74089748,10.21376133,
            true, 1000, null, null, null, null, null
        ),
        BadestedEntity(0, "Helleneset", "Bergen", 60.43513,5.27953,
            false, 10000, null, null, null, null, null
        ),
        BadestedEntity(0, "Huk", "Oslo", 59.89534,10.67733,
            true, 0, null, null, null, null, null
        ),
        BadestedEntity(0, "Sørenga", "Oslo", 59.90284732,10.75208716,
            false, 100, null, null, null, null, null
        ),
    )

    @Before
    fun lagDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        badestedDao = db.badestedDao()

        for (badested in badesteder)
            badestedDao.lagre(badested)
    }

    @After
    @Throws(IOException::class)
    fun lukkDatabase() {
        db.close()
    }

    @Test
    fun hentBadesteder() {
        val liste = badestedDao.hentBadestedListe()

        //Sjekk at alle badestedene blir returnert.
        for (badested in badesteder)
            assertNotEquals(null, liste.find { it.navn == badested.navn})
    }

    @Test
    fun sokFungerer() {
        var resultat = badestedDao.finnBadesteder("H%")

        //Sjekk at søk etter badested fungerer
        assertEquals(2, resultat.size)
        assertNotEquals(null, resultat.find { it.navn == "Huk"})
        assertNotEquals(null, resultat.find { it.navn == "Helleneset"})

        //Sjekk at stedssøk fungerer
        resultat = badestedDao.finnBadesteder("Oslo")
        assertEquals(2, resultat.size)
        assertNotEquals(null, resultat.find { it.navn == "Huk"})
        assertNotEquals(null, resultat.find { it.navn == "Sørenga"})
    }

    @Test
    fun hentNaermeste() {
        val result = badestedDao.hentNaermeste()
        assertEquals(true, result[0].navn == "Huk")
        assertEquals(true, result[1].navn == "Sørenga")
        assertEquals(true, result[2].navn == "Bragernes strand")
        assertEquals(true, result[3].navn == "Helleneset")
    }

    @Test
    fun hentFavoritter() {
        val resultat = badestedDao.hentFavoritterListe()
        assertNotEquals(null, resultat.find { it.navn == "Bragernes strand"})
        assertNotEquals(null, resultat.find { it.navn == "Huk"})
        assertEquals(null, resultat.find { it.navn == "Helleneset"})
    }

    @Test
    fun idGenereresAutomatisk() {
        val badesteder = badestedDao.hentBadestedListe()
        assertEquals(true, badesteder[0].id == 1)
        assertEquals(true, badesteder[1].id == 2)
    }

    @Test
    fun oppdaterAvstand() {
        badestedDao.oppdaterAvstand(AvstandsOppdatering(3, 123456)) //Endrer avstanden til Huk
        badestedDao.oppdaterAvstand(AvstandsOppdatering(2, 10))     //Endrer avstanden til Helleneset

        val badesteder = badestedDao.hentNaermeste()
        assertEquals(true, badesteder[0].navn == "Helleneset")
        assertEquals(true, badesteder[3].navn == "Huk")
    }

    @Test
    fun fjernOgLeggTilFavoritt() {
        //Fjerner alle favoritter og sjekker at ingen badesteder fortsatt er favoritt.
        var badesteder = badestedDao.hentBadestedListe()
        for (badested in badesteder) {
            badestedDao.fjernFavoritt(badested.id)
            assertEquals(false, badestedDao.hentBadested(badested.id).favoritt)
        }

        //Legget til badestedet med id 3 som favoritt
        badestedDao.leggTilFavoritt(3)
        badesteder = badestedDao.hentBadestedListe()

        assertTrue(badesteder.find { it.favoritt }!!.id == 3)
    }

    @Test
    fun hentBadestedMedId() {
        val badested = badestedDao.hentBadested(3)
        assertEquals("Huk", badested.navn)
    }

    @Test
    fun oppdaterForecast() {
        val forecaster = listOf(BadestedForecast(10.2, 15.2, "cloudy", "10 m/s", "2021-04-29T13:00:00Z"))
        badestedDao.oppdaterBadeforhold(ForecastOppdatering(1, forecaster, (System.currentTimeMillis()/1000).toString()))

        //Sjekker at badestedet med riktig id har værvarselet
        assertNotNull(badestedDao.hentBadested(1).badeforhold?.get(0))
        assertNull(badestedDao.hentBadested(2).badeforhold?.get(0))

        //Sjekk at værvarselet inneholder forventet verdi.
        assertEquals(10.2, badestedDao.hentBadested(1).badeforhold?.get(0)?.havTemperatur)

    }
}