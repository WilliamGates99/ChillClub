package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.core.data.local.repositories.FakeConnectivityObserverImpl
import com.xeniac.chillclub.core.data.local.repositories.FakePreferencesRepositoryImpl
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositorySingletonModule::class]
)
abstract class FakeRepositorySingletonModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        fakePreferencesRepositoryImpl: FakePreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        fakeConnectivityObserverImpl: FakeConnectivityObserverImpl
    ): ConnectivityObserver
}