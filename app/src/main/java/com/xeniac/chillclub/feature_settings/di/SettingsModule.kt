package com.xeniac.chillclub.feature_settings.di

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetCurrentAppThemeUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetNotificationPermissionCountUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.SettingsUseCases
import com.xeniac.chillclub.feature_settings.domain.use_cases.StoreCurrentAppThemeUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.StoreIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.StoreNotificationPermissionCountUseCase
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
        preferencesRepository: PreferencesRepository
    ): GetCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsPlayInBackgroundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsPlayInBackgroundEnabledUseCase =
        GetIsPlayInBackgroundEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): GetNotificationPermissionCountUseCase =
        GetNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreCurrentAppThemeUseCase = StoreCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreIsPlayInBackgroundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreIsPlayInBackgroundEnabledUseCase =
        StoreIsPlayInBackgroundEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreNotificationPermissionCountUseCase =
        StoreNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase,
        getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        storeCurrentAppThemeUseCase: StoreCurrentAppThemeUseCase,
        storeIsPlayInBackgroundEnabledUseCase: StoreIsPlayInBackgroundEnabledUseCase,
        storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentAppThemeUseCase },
        { getIsPlayInBackgroundEnabledUseCase },
        { getNotificationPermissionCountUseCase },
        { storeCurrentAppThemeUseCase },
        { storeIsPlayInBackgroundEnabledUseCase },
        { storeNotificationPermissionCountUseCase }
    )
}