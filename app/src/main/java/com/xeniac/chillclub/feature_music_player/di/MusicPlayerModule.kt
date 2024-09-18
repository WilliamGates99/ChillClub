package com.xeniac.chillclub.feature_music_player.di

import android.media.AudioManager
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.data.local.RadioStationsDao
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.use_cases.AdjustMusicVolumeUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetCurrentlyPlayingRadioStationIdUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetCurrentlyPlayingRadioStationUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetNotificationPermissionCountUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetRadioStationsUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.domain.use_cases.ObserveMusicVolumeChangesUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.StoreCurrentlyPlayingRadioStationIdUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.StoreNotificationPermissionCountUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

typealias MUSIC_STREAM_TYPE = Int

@Module
@InstallIn(ViewModelComponent::class)
object MusicPlayerModule {

    @Provides
    @ViewModelScoped
    fun provideRadioStationsDao(
        database: ChillClubDatabase
    ): RadioStationsDao = database.radioStationsDao

    @Provides
    @ViewModelScoped
    fun provideMusicStreamType(): MUSIC_STREAM_TYPE = AudioManager.STREAM_MUSIC

    @Provides
    @ViewModelScoped
    fun provideObserveMusicVolumeChangesUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): ObserveMusicVolumeChangesUseCase = ObserveMusicVolumeChangesUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideAdjustMusicVolumeUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): AdjustMusicVolumeUseCase = AdjustMusicVolumeUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetRadioStationsUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): GetRadioStationsUseCase = GetRadioStationsUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetCurrentlyPlayingRadioStationIdUseCase(
        musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
    ): GetCurrentlyPlayingRadioStationIdUseCase =
        GetCurrentlyPlayingRadioStationIdUseCase(musicPlayerDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetCurrentlyPlayingRadioStationUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): GetCurrentlyPlayingRadioStationUseCase =
        GetCurrentlyPlayingRadioStationUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsPlayInBackgroundEnabledUseCase =
        GetIsPlayInBackgroundEnabledUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetNotificationPermissionCountUseCase =
        GetNotificationPermissionCountUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentlyPlayingRadioStationIdUseCase(
        musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
    ): StoreCurrentlyPlayingRadioStationIdUseCase =
        StoreCurrentlyPlayingRadioStationIdUseCase(musicPlayerDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreNotificationPermissionCountUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): StoreNotificationPermissionCountUseCase =
        StoreNotificationPermissionCountUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideMusicPlayerUseCases(
        observeMusicVolumeChangesUseCase: ObserveMusicVolumeChangesUseCase,
        adjustMusicVolumeUseCase: AdjustMusicVolumeUseCase,
        getRadioStationsUseCase: GetRadioStationsUseCase,
        getCurrentlyPlayingRadioStationIdUseCase: GetCurrentlyPlayingRadioStationIdUseCase,
        getCurrentlyPlayingRadioStationUseCase: GetCurrentlyPlayingRadioStationUseCase,
        getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        storeCurrentlyPlayingRadioStationIdUseCase: StoreCurrentlyPlayingRadioStationIdUseCase,
        storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase
    ): MusicPlayerUseCases = MusicPlayerUseCases(
        { observeMusicVolumeChangesUseCase },
        { adjustMusicVolumeUseCase },
        { getRadioStationsUseCase },
        { getCurrentlyPlayingRadioStationIdUseCase },
        { getCurrentlyPlayingRadioStationUseCase },
        { getIsPlayInBackgroundEnabledUseCase },
        { getNotificationPermissionCountUseCase },
        { storeCurrentlyPlayingRadioStationIdUseCase },
        { storeNotificationPermissionCountUseCase }
    )
}