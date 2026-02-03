package com.xeniac.chillclub.feature_music_player.di

import android.media.AudioManager
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.data.local.RadioStationsDao
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
}