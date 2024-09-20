package com.xeniac.chillclub.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.data.local.entities.RadioStationsVersionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioStationsDao {

    @Transaction
    suspend fun replaceAllRadioStations(
        radioStationsVersionEntity: RadioStationsVersionEntity,
        radioStationEntities: List<RadioStationEntity>
    ) {
        clearRadioStationsVersions()
        insertRadioStationsVersion(radioStationsVersionEntity)

        clearRadioStations()
        insertRadioStations(radioStationEntities)
    }

    @Upsert
    suspend fun insertRadioStationsVersion(radioStationsVersionEntity: RadioStationsVersionEntity): Long

    @Upsert
    suspend fun insertRadioStation(radioStationEntity: RadioStationEntity): Long

    @Upsert
    suspend fun insertRadioStations(radioStationEntities: List<RadioStationEntity>)

    @Query("DELETE FROM radio_stations_version")
    suspend fun clearRadioStationsVersions()

    @Query("DELETE FROM radio_stations")
    suspend fun clearRadioStations()

    @Delete
    suspend fun deleteRadioStation(radioStationEntity: RadioStationEntity)

    @Query("DELETE FROM radio_stations WHERE id = :radioId")
    suspend fun deleteRadioStationById(radioId: Long)

    @Query("SELECT * FROM radio_stations_version")
    suspend fun getRadioStationsVersions(): List<RadioStationsVersionEntity>

    @Query("SELECT * FROM radio_stations")
    suspend fun getRadioStations(): List<RadioStationEntity>

    @Query("SELECT * FROM radio_stations")
    fun observeRadioStations(): Flow<List<RadioStationEntity>>

    @Query("SELECT * FROM radio_stations WHERE id = :id")
    fun observeRadioStation(id: Long): Flow<RadioStationEntity?>
}