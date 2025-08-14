package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.core.data.repositories.FakeConnectivityObserverImpl
import com.xeniac.chillclub.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.chillclub.core.data.repositories.FakeMusicPlayerDataStoreRepositoryImpl
import com.xeniac.chillclub.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositorySingletonModule {

    @Binds
    @Singleton
    abstract fun bindSettingsDataStoreRepository(
        settingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    ): SettingsDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindMusicPlayerDataStoreRepository(
        musicPlayerDataStoreRepositoryImpl: FakeMusicPlayerDataStoreRepositoryImpl
    ): MusicPlayerDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindMiscellaneousPreferencesRepository(
        miscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    ): MiscellaneousDataStoreRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        fakeConnectivityObserverImpl: FakeConnectivityObserverImpl
    ): ConnectivityObserver
}