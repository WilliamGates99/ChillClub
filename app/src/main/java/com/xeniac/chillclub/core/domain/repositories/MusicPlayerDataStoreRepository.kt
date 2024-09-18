package com.xeniac.chillclub.core.domain.repositories

import kotlinx.coroutines.flow.Flow

typealias RadioStationId = Long

interface MusicPlayerDataStoreRepository {

    fun getCurrentlyPlayingRadioStationId(): Flow<RadioStationId?>

    suspend fun storeCurrentlyPlayingRadioStationId(id: Long)
}