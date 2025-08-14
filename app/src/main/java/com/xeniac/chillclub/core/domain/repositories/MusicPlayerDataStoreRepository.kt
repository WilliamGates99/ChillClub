package com.xeniac.chillclub.core.domain.repositories

import com.xeniac.chillclub.core.domain.models.RadioStationId
import kotlinx.coroutines.flow.Flow

interface MusicPlayerDataStoreRepository {

    fun getCurrentlyPlayingRadioStationId(): Flow<RadioStationId?>

    suspend fun storeCurrentlyPlayingRadioStationId(id: Long)

    suspend fun removeCurrentlyPlayingRadioStationId()
}