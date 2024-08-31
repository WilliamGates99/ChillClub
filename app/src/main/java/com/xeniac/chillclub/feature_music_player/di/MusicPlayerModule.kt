package com.xeniac.chillclub.feature_music_player.di

import android.media.AudioManager
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.use_cases.DecreaseMusicVolumeUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetNotificationPermissionCountUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetRadiosUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.IncreaseMusicVolumeUseCase
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
    fun provideMusicStreamType(): MUSIC_STREAM_TYPE = AudioManager.STREAM_MUSIC

    @Provides
    @ViewModelScoped
    fun provideObserveMusicVolumeChangesUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): ObserveMusicVolumeChangesUseCase = ObserveMusicVolumeChangesUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideDecreaseMusicVolumeUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): DecreaseMusicVolumeUseCase = DecreaseMusicVolumeUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideIncreaseMusicVolumeUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): IncreaseMusicVolumeUseCase = IncreaseMusicVolumeUseCase(musicPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetRadiosUseCase(
        musicPlayerRepository: MusicPlayerRepository
    ): GetRadiosUseCase = GetRadiosUseCase(musicPlayerRepository)

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
        decreaseMusicVolumeUseCase: DecreaseMusicVolumeUseCase,
        increaseMusicVolumeUseCase: IncreaseMusicVolumeUseCase,
        getRadiosUseCase: GetRadiosUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        storeNotificationPermissionCountUseCase: StoreNotificationPermissionCountUseCase
    ): MusicPlayerUseCases = MusicPlayerUseCases(
        { observeMusicVolumeChangesUseCase },
        { decreaseMusicVolumeUseCase },
        { increaseMusicVolumeUseCase },
        { getRadiosUseCase },
        { getNotificationPermissionCountUseCase },
        { storeNotificationPermissionCountUseCase }
    )
}