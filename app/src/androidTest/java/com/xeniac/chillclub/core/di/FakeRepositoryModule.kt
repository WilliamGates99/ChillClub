package com.xeniac.chillclub.core.di

import com.xeniac.chillclub.feature_music_player.data.remote.repositories.FakeMusicPlayerRepositoryImpl
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindMusicPlayerRepository(
        fakeMusicPlayerRepositoryImpl: FakeMusicPlayerRepositoryImpl
    ): MusicPlayerRepository
}