package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.core.data.repositories.ConnectivityObserverImpl
import com.xeniac.chillclub.core.data.repositories.MiscellaneousDataStoreRepositoryImpl
import com.xeniac.chillclub.core.data.repositories.MusicPlayerDataStoreRepositoryImpl
import com.xeniac.chillclub.core.data.repositories.SettingsDataStoreRepositoryImpl
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositorySingletonModule {

    @Binds
    @Singleton
    abstract fun bindSettingsDataStoreRepository(
        settingsDataStoreRepositoryImpl: SettingsDataStoreRepositoryImpl
    ): SettingsDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindMusicPlayerDataStoreRepository(
        musicPlayerDataStoreRepositoryImpl: MusicPlayerDataStoreRepositoryImpl
    ): MusicPlayerDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindMiscellaneousPreferencesRepository(
        miscellaneousDataStoreRepositoryImpl: MiscellaneousDataStoreRepositoryImpl
    ): MiscellaneousDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl
    ): ConnectivityObserver
}