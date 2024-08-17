package com.xeniac.chillclub.feature_music_player.domain.repositories

import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError

interface MusicPlayerRepository {

    suspend fun getRadios(): Result<List<Radio>, GetRadiosError>

    sealed class EndPoints(val url: String) {
        data object GetRadios : EndPoints(url = "${BuildConfig.KTOR_HTTP_BASE_URL}/radio.json")
    }
}