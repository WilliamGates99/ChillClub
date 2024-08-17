package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.feature_music_player.data.remote.repositories.MusicPlayerRepositoryImpl
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindMusicPlayerRepository(
        musicPlayerRepositoryImpl: MusicPlayerRepositoryImpl
    ): MusicPlayerRepository
}