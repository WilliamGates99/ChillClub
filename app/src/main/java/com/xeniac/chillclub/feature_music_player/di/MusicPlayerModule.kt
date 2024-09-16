package com.xeniac.chillclub.feature_music_player.di

import android.media.AudioManager
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.data.local.RadioStationsDao
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.use_cases.AdjustMusicVolumeUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetIsPlayInBackgroundEnabledUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetNotificationPermissionCountUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetRadioStationsUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.domain.use_cases.ObserveMusicVolumeChangesUseCase
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
    fun provideStoreNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreNotificationPermissionCountUseCase =
        StoreNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideMusicPlayerUseCases(
        observeMusicVolumeChangesUseCase: ObserveMusicVolumeChangesUseCase,
        adjustMusicVolumeUseCase: AdjustMusicVolumeUseCase,
        getRadioStationsUseCase: GetRadioStationsUseCase,
        getIsPlayInBackgroundEnabledUseCase: GetIsPlayInBackgroundEnabledUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase
    ): MusicPlayerUseCases = MusicPlayerUseCases(
        { observeMusicVolumeChangesUseCase },
        { adjustMusicVolumeUseCase },
        { getRadioStationsUseCase },
        { getIsPlayInBackgroundEnabledUseCase },
        { getNotificationPermissionCountUseCase },
        { storeNotificationPermissionCountUseCase }
    )
}