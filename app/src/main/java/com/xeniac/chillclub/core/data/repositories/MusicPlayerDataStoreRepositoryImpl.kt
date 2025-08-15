package com.xeniac.chillclub.core.data.repositories

import androidx.datastore.core.DataStore
import com.xeniac.chillclub.core.domain.models.MusicPlayerPreferences
import com.xeniac.chillclub.core.domain.models.RadioStationId
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class MusicPlayerDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<MusicPlayerPreferences>
) : MusicPlayerDataStoreRepository {

    override fun getCurrentlyPlayingRadioStationId(): Flow<RadioStationId?> = dataStore.data.map {
        it.currentlyPlayingRadioStationId
    }.catch { e ->
        Timber.e("Get currently playing radio station ID failed:")
        e.printStackTrace()
    }

    override suspend fun storeCurrentlyPlayingRadioStationId(id: Long) {
        try {
            dataStore.updateData { it.copy(currentlyPlayingRadioStationId = id) }
            Timber.i("Currently playing radio station ID edited to $id")
        } catch (e: Exception) {
            Timber.e("Store currently playing radio station ID failed:")
            e.printStackTrace()
        }
    }

    override suspend fun removeCurrentlyPlayingRadioStationId() {
        try {
            dataStore.updateData { it.copy(currentlyPlayingRadioStationId = null) }
            Timber.i("Currently playing radio station ID removed")
        } catch (e: Exception) {
            Timber.e("Remove currently playing radio station ID failed:")
            e.printStackTrace()
        }
    }
}