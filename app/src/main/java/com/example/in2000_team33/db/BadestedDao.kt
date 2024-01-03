package com.example.in2000_team33.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BadestedDao {
    @Query("SELECT * FROM badestedentity")
    fun hentBadesteder(): Flow<List<BadestedEntity>>

    @Query("SELECT * FROM badestedentity")
    fun hentBadestedListe(): List<BadestedEntity>

    @Query("SELECT * FROM badestedentity WHERE navn LIKE :input OR sted LIKE :input")
    fun finnBadesteder(input: String): List<BadestedEntity>

    @Query("SELECT * FROM badestedentity ORDER BY avstand")
    fun hentNaermeste(): List<BadestedEntity>

    @Query("UPDATE badestedentity  SET favoritt = 1 WHERE id = :placeId")
    fun leggTilFavoritt(placeId: Int)

    @Query("UPDATE badestedentity SET favoritt = 0 WHERE id = :placeId")
    fun fjernFavoritt(placeId: Int)

    @Query("SELECT * FROM badestedentity WHERE favoritt = 1")
    fun hentFavoritter(): Flow<List<BadestedEntity>>

    @Query("SELECT * FROM badestedentity WHERE favoritt = 1")
    fun hentFavoritterListe(): List<BadestedEntity>

    @Update(entity = BadestedEntity::class)
    fun oppdaterBadeforhold(forecastliste: ForecastOppdatering)

    @Update(entity = BadestedEntity::class)
    fun oppdaterSunrise(oppdatert: SunriseOppdatering)

    @Update(entity = BadestedEntity::class)
    fun oppdaterObservertTemperatur(temperatur: ObservertTemperaturOppdatering)

    @Update(entity = BadestedEntity::class)
    fun oppdaterAvstand(avstand: AvstandsOppdatering)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun lagre(badesteder: BadestedEntity)

    @Query("SELECT * FROM badestedentity WHERE id = :placeId")
    fun hentBadested(placeId: Int): BadestedEntity
}

