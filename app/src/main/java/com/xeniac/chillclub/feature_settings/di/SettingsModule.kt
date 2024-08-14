package com.xeniac.chillclub.feature_settings.di

import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetCurrentAppThemeUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.GetIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.SetCurrentAppThemeUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.SetIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_settings.domain.use_cases.SettingsUseCases
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
    fun provideSetCurrentAppThemeUseCase(
        preferencesRepository: PreferencesRepository
    ): SetCurrentAppThemeUseCase = SetCurrentAppThemeUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetIsPlayInBackgroundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): SetIsPlayInBackgroundEnabledUseCase =
        SetIsPlayInBackgroundEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSettingsUseCases(
        setCurrentAppThemeUseCase: SetCurrentAppThemeUseCase,
        getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase,
        getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase,
        setIsPlayInBackgroundEnabledUseCase: SetIsPlayInBackgroundEnabledUseCase
    ): SettingsUseCases = SettingsUseCases(
        { getCurrentAppThemeUseCase },
        { getIsPlayInBackgroundEnabledUseCase },
        { setCurrentAppThemeUseCase },
        { setIsPlayInBackgroundEnabledUseCase }
    )
}