package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.core.data.repositories.ConnectivityObserverImpl
import com.xeniac.chillclub.core.data.repositories.PreferencesRepositoryImpl
import com.xeniac.chillclub.core.domain.repositories.ConnectivityObserver
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
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
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl
    ): ConnectivityObserver
}