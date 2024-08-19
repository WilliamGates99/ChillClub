package com.xeniac.chillclub.feature_music_player.di

import android.content.Context
import androidx.room.Room
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import com.xeniac.chillclub.feature_music_player.data.db.ChillClubDao
import com.xeniac.chillclub.feature_music_player.data.db.ChillClubDatabase
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetNotificationPermissionCountUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.GetRadiosUseCase
import com.xeniac.chillclub.feature_music_player.domain.use_cases.MusicPlayerUseCases
import com.xeniac.chillclub.feature_music_player.domain.use_cases.SetNotificationPermissionCountUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MusicPlayerModule {

    @Provides
    @ViewModelScoped
    fun provideChillClubDatabase(
        @ApplicationContext context: Context
    ): ChillClubDatabase = Room.databaseBuilder(
        context = context,
        klass = ChillClubDatabase::class.java,
        name = "ChillClub_db"
    ).build()

    @Provides
    @ViewModelScoped
    fun provideChillClubDao(
        database: ChillClubDatabase
    ): ChillClubDao = database.dao()

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
    fun provideSetNotificationPermissionCountUseCase(
        preferencesRepository: PreferencesRepository
    ): SetNotificationPermissionCountUseCase =
        SetNotificationPermissionCountUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideMusicPlayerUseCases(
        getRadiosUseCase: GetRadiosUseCase,
        getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase,
        setNotificationPermissionCountUseCase: SetNotificationPermissionCountUseCase
    ): MusicPlayerUseCases = MusicPlayerUseCases(
        { getRadiosUseCase },
        { getNotificationPermissionCountUseCase },
        { setNotificationPermissionCountUseCase }
    )
}