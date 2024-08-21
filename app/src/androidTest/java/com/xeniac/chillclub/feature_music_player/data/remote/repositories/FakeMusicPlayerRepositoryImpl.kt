package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeMusicPlayerRepositoryImpl @Inject constructor() : MusicPlayerRepository {

    override suspend fun getRadios(
        fetchFromRemote: Boolean
    ): Flow<Result<List<Radio>, GetRadiosError>> = flow {
        emit(Result.Success(emptyList()))
    }
}