package com.xeniac.chillclub.feature_music_player.di

import android.media.AudioManager
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.data.local.RadioStationsDao
import com.xeniac.chillclub.core.domain.repositories.MusicPlayerDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.PermissionsDataStoreRepository
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumeRepository
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
    fun provideGetRadioStationsUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): GetRadioStationsUseCase = GetRadioStationsUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetCurrentlyPlayingRadioStationUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): GetCurrentlyPlayingRadioStationUseCase = GetCurrentlyPlayingRadioStationUseCase(
        musicPlayerRepository
    )

    @Provides
    @ViewModelScoped
    fun provideObserveMusicVolumeChangesUseCase(
        musicVolumeRepository: MusicVolumeRepository
    ): ObserveMusicVolumeChangesUseCase = ObserveMusicVolumeChangesUseCase(musicVolumeRepository)

    @Provides
    @ViewModelScoped
    fun provideAdjustMusicVolumeUseCase(
        musicVolumeRepository: MusicVolumeRepository
    ): AdjustMusicVolumeUseCase = AdjustMusicVolumeUseCase(musicVolumeRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsPlayInBackgroundEnabledUseCase = GetIsPlayInBackgroundEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetCurrentlyPlayingRadioStationIdUseCase(
        musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
    ): GetCurrentlyPlayingRadioStationIdUseCase = GetCurrentlyPlayingRadioStationIdUseCase(
        musicPlayerDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreCurrentlyPlayingRadioStationIdUseCase(
        musicPlayerDataStoreRepository: MusicPlayerDataStoreRepository
    ): StoreCurrentlyPlayingRadioStationIdUseCase = StoreCurrentlyPlayingRadioStationIdUseCase(
        musicPlayerDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetNotificationPermissionCountUseCase(
        permissionsDataStoreRepository: PermissionsDataStoreRepository
    ): GetNotificationPermissionCountUseCase = GetNotificationPermissionCountUseCase(
        permissionsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreNotificationPermissionCountUseCase(
        permissionsDataStoreRepository: PermissionsDataStoreRepository
    ): StoreNotificationPermissionCountUseCase = StoreNotificationPermissionCountUseCase(
        permissionsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideMusicPlayerUseCases(
        getRadioStationsUseCase: GetRadioStationsUseCase,
        getCurrentlyPlayingRadioStationUseCase: GetCurrentlyPlayingRadioStationUseCase,
        observeMusicVolumeChangesUseCase: ObserveMusicVolumeChangesUseCase,
        adjustMusicVolumeUseCase: AdjustMusicVolumeUseCase,
        getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase,
        getCurrentlyPlayingRadioStationIdUseCase: GetCurrentlyPlayingRadioStationIdUseCase,
        storeCurrentlyPlayingRadioStationIdUseCase: StoreCurrentlyPlayingRadioStationIdUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase
    ): MusicPlayerUseCases = MusicPlayerUseCases(
        { getRadioStationsUseCase },
        { getCurrentlyPlayingRadioStationUseCase },
        { observeMusicVolumeChangesUseCase },
        { adjustMusicVolumeUseCase },
        { getIsPlayInBackgroundEnabledUseCase },
        { getCurrentlyPlayingRadioStationIdUseCase },
        { storeCurrentlyPlayingRadioStationIdUseCase },
        { getNotificationPermissionCountUseCase },
        { storeNotificationPermissionCountUseCase }
    )
}