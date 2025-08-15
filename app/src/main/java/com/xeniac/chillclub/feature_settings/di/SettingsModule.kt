package com.xeniac.chillclub.feature_settings.di

import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetCurrentAppThemeUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.SettingsUseCases
import com.xeniac.chillclub.feature_settings.domain.use_cases.StoreCurrentAppThemeUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.StoreIsPlayInBackgroundEnabledUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object SettingsModule {

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppThemeUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsPlayInBackgroundEnabledUseCase = GetIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentAppThemeUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreCurrentAppThemeUseCase = StoreCurrentAppThemeUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreIsPlayInBackgroundEnabledUseCase = StoreIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase,
        getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase,
        storeCurrentAppThemeUseCase: StoreCurrentAppThemeUseCase,
        storeIsPlayInBackgroundEnabledUseCase: StoreIsPlayInBackgroundEnabledUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentAppThemeUseCase },
        { getIsPlayInBackgroundEnabledUseCase },
        { storeCurrentAppThemeUseCase },
        { storeIsPlayInBackgroundEnabledUseCase }
    )
}