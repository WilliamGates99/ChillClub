package com.xeniac.chillclub.core.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.xeniac.chillclub.core.di.MusicPlayerDataStoreQualifier
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.RadioStationId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class MusicPlayerDataStoreRepositoryImpl @Inject constructor(
    @MusicPlayerDataStoreQualifier private val dataStore: DataStore<Preferences>
) : MusicPlayerDataStoreRepository {

    private object PreferencesKeys {
        val CURRENTLY_PLAYING_RADIO_STATION_ID = longPreferencesKey(
            name = "currentlyPlayingRadioStationId"
        )
    }

    override fun getCurrentlyPlayingRadioStationId(): Flow<RadioStationId?> =
        dataStore.data.map {
            it[PreferencesKeys.CURRENTLY_PLAYING_RADIO_STATION_ID]
        }.catch { e ->
            Timber.e("getNotificationPermissionCount failed:")
            e.printStackTrace()
        }

    override suspend fun storeCurrentlyPlayingRadioStationId(id: Long) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.CURRENTLY_PLAYING_RADIO_STATION_ID] = id
                Timber.i("Currently playing radio station id edited to $id")
            }
        } catch (e: Exception) {
            Timber.e("storeCurrentlyPlayingRadioStationId failed:")
            e.printStackTrace()
        }
    }
}