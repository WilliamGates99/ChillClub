package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.data.mappers.toRadioStation
import com.xeniac.chillclub.core.data.mappers.toRadioStationDto
import com.xeniac.chillclub.core.data.mappers.toRadioStationEntity
import com.xeniac.chillclub.core.data.utils.createKtorTestClient
import com.xeniac.chillclub.core.domain.models.Channel
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.Result
import com.xeniac.chillclub.core.domain.models.SocialLinks
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadioStationsResponseDto
import com.xeniac.chillclub.feature_music_player.domain.errors.GetRadioStationsError
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeMusicPlayerRepositoryImpl @Inject constructor() : MusicPlayerRepository {

    private var isNetworkAvailable = true
    private var getRadioStationsHttpStatusCode = HttpStatusCode.OK

    private var radioStationEntities = SnapshotStateList<RadioStationEntity>()

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setGetRadioStationsHttpStatusCode(httpStatusCode: HttpStatusCode) {
        getRadioStationsHttpStatusCode = httpStatusCode
    }

    fun addDummyRadioStations() {
        repeat(times = 10) { index ->
            val radioStationEntity = RadioStationEntity(
                youtubeVideoId = "videoId$index",
                title = "Test Title $index",
                channel = Channel(
                    name = "Test Channel $index",
                    avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                    socialLinks = SocialLinks(
                        youtube = "https://www.youtube.com"
                    )
                ),
                category = "Test",
                tags = listOf("tag1", "tag2"),
                id = index.toLong()
            )

            radioStationEntities.add(radioStationEntity)
        }
    }

    override fun getRadioStations(
        fetchFromRemote: Boolean
    ): Flow<Result<List<RadioStation>, GetRadioStationsError>> = flow {
        val shouldJustLoadFromCache = radioStationEntities.isNotEmpty() && !fetchFromRemote
        if (shouldJustLoadFromCache) {
            emit(Result.Success(radioStationEntities.map { it.toRadioStation() }))
            return@flow
        }

        if (!isNetworkAvailable) {
            emit(Result.Error(GetRadioStationsError.Network.Offline))
            return@flow
        }

        val mockEngine = MockEngine {
            val isResponseHttpStatusOk = getRadioStationsHttpStatusCode == HttpStatusCode.OK
            val getRadioStationsResponseDto = if (isResponseHttpStatusOk) {
                addDummyRadioStations()
                GetRadioStationsResponseDto(
                    version = 1,
                    radioStationDtos = radioStationEntities.map { it.toRadioStationDto() })
            } else {
                GetRadioStationsResponseDto(
                    version = 1,
                    radioStationDtos = emptyList()
                )
            }

            respond(
                content = Json.encodeToString(getRadioStationsResponseDto),
                status = getRadioStationsHttpStatusCode,
                headers = headersOf(
                    name = HttpHeaders.ContentType,
                    value = ContentType.Application.Json.toString()
                )
            )
        }

        val httpResponse = createKtorTestClient(mockEngine = mockEngine).get(
            urlString = MusicPlayerRepository.EndPoints.GetRadioStations.url
        )

        return@flow when (httpResponse.status) {
            HttpStatusCode.OK -> {
                val remoteRadioDtos = httpResponse
                    .body<GetRadioStationsResponseDto>()
                    .radioStationDtos

                radioStationEntities.clear()
                radioStationEntities.addAll(remoteRadioDtos.map { it.toRadioStationEntity() })

                emit(Result.Success(radioStationEntities.map { it.toRadioStation() }))
            }
            else -> emit(Result.Error(GetRadioStationsError.Network.SomethingWentWrong))
        }
    }

    override fun getCurrentlyPlayingRadioStation(
        radioStationId: Long
    ): Flow<RadioStation?> = flow {
        val currentRadioStation = radioStationEntities.find {
            it.id == radioStationId
        }?.toRadioStation()

        currentRadioStation?.let { emit(it) }
    }
}