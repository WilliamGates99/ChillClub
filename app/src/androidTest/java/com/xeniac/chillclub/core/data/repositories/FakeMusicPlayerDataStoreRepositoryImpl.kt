package com.xeniac.chillclub.core.data.repositories

import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.RadioStationId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeMusicPlayerDataStoreRepositoryImpl @Inject constructor(
) : MusicPlayerDataStoreRepository {

    var currentlyPlayingRadioStationId: RadioStationId? = null

    override fun getCurrentlyPlayingRadioStationId(): Flow<RadioStationId?> = flow {
        emit(currentlyPlayingRadioStationId)
    }

    override suspend fun storeCurrentlyPlayingRadioStationId(id: Long) {
        currentlyPlayingRadioStationId = id
    }

    override suspend fun removeCurrentlyPlayingRadioStationId() {
        currentlyPlayingRadioStationId = null
    }
}