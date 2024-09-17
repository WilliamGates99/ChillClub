package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.core.domain.use_cases.GetCurrentAppLocaleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object CoreModule {

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppLocaleUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(settingsDataStoreRepository)
}