package com.xeniac.chillclub.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.domain.models.RadioStationId
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import kotlinx.coroutines.flow.Flow

class FakeMusicPlayerDataStoreRepositoryImpl : MusicPlayerDataStoreRepository {

    var currentlyPlayingRadioStationId = SnapshotStateList<RadioStationId?>().apply {
        add(null)
    }

    override fun getCurrentlyPlayingRadioStationId(): Flow<RadioStationId?> = snapshotFlow {
        currentlyPlayingRadioStationId.first()
    }

    override suspend fun storeCurrentlyPlayingRadioStationId(id: Long) {
        currentlyPlayingRadioStationId.apply {
            clear()
            add(id)
        }
    }

    override suspend fun removeCurrentlyPlayingRadioStationId() {
        currentlyPlayingRadioStationId.apply {
            clear()
            add(null)
        }
    }
}