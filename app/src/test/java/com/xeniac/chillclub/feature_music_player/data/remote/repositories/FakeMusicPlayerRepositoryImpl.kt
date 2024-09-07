package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.data.utils.convertToPercentage
import com.xeniac.chillclub.core.domain.models.Channel
import com.xeniac.chillclub.core.domain.models.RadioStation
import com.xeniac.chillclub.core.domain.models.SocialLinks
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadioStationsResponseDto
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolumePercentage
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadioStationsError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeMusicPlayerRepositoryImpl @Inject constructor() : MusicPlayerRepository {

    private var isNetworkAvailable = true
    private var getRadioStationsHttpStatusCode = HttpStatusCode.OK

    private var minVolume = 0
    private var maxVolume = 15
    var currentMusicVolume = 7
    var musicVolumePercentage = SnapshotStateList<MusicVolumePercentage>().apply {
        add(
            currentMusicVolume.convertToPercentage(
                minValue = minVolume,
                maxValue = maxVolume
            )
        )
    }
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

    override fun observeMusicVolumeChanges(): Flow<MusicVolumePercentage> = snapshotFlow {
        musicVolumePercentage.first()
    }

    override suspend fun decreaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>> =
        callbackFlow {
            try {
                currentMusicVolume -= 1

                musicVolumePercentage.apply {
                    clear()
                    add(
                        (currentMusicVolume).convertToPercentage(
                            minValue = currentMusicVolume,
                            maxValue = maxVolume
                        )
                    )
                }

                send(Result.Success(Unit))
            } catch (e: Exception) {
                send(Result.Error(AdjustVolumeError.SomethingWentWrong))
            }

            awaitClose {}
        }

    override suspend fun increaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>> =
        callbackFlow {
            try {
                currentMusicVolume += 1

                musicVolumePercentage.apply {
                    clear()
                    add(
                        (currentMusicVolume).convertToPercentage(
                            minValue = currentMusicVolume,
                            maxValue = maxVolume
                        )
                    )
                }

                send(Result.Success(Unit))
            } catch (e: Exception) {
                send(Result.Error(AdjustVolumeError.SomethingWentWrong))
            }

            awaitClose {}
        }

    override suspend fun getRadioStations(
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
                GetRadioStationsResponseDto(radioStationDtos = radioStationEntities.map { it.toRadioStationDto() })
            } else {
                GetRadioStationsResponseDto(radioStationDtos = emptyList())
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

        val testClient = HttpClient(engine = mockEngine) {
            install(ContentNegotiation) {
                register(
                    contentType = ContentType.Text.Plain,
                    converter = KotlinxSerializationConverter(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        coerceInputValues = true
                        isLenient = true
                    })
                )
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    coerceInputValues = true
                    isLenient = true
                })
            }
            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
            }
        }

        val response = testClient.get(
            urlString = MusicPlayerRepository.EndPoints.GetRadioStations.url
        )

        when (response.status) {
            HttpStatusCode.OK -> {
                val remoteRadioDtos = response.body<GetRadioStationsResponseDto>().radioStationDtos

                radioStationEntities.clear()
                radioStationEntities.addAll(remoteRadioDtos.map { it.toRadioStationEntity() })

                emit(Result.Success(radioStationEntities.map { it.toRadioStation() }))
            }
            else -> emit(Result.Error(GetRadioStationsError.Network.SomethingWentWrong))
        }
    }
}