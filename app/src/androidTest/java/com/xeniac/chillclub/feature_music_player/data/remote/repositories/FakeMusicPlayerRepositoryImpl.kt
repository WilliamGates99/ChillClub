package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.data.local.entities.RadioEntity
import com.xeniac.chillclub.core.domain.models.Channel
import com.xeniac.chillclub.core.domain.models.Radio
import com.xeniac.chillclub.core.domain.models.SocialLinks
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadiosResponseDto
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicVolume
import com.xeniac.chillclub.feature_music_player.domain.utils.AdjustVolumeError
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
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
    private var getRadiosHttpStatusCode = HttpStatusCode.OK

    private var musicVolume = SnapshotStateList<MusicVolume>().apply { add(5) }
    private var radioEntities = SnapshotStateList<RadioEntity>()

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setGetRadiosHttpStatusCode(httpStatusCode: HttpStatusCode) {
        getRadiosHttpStatusCode = httpStatusCode
    }

    fun addDummyRadios() {
        repeat(times = 10) { index ->
            val radioEntity = RadioEntity(
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

            radioEntities.add(radioEntity)
        }
    }

    override fun observeMusicVolumeChanges(): Flow<MusicVolume> = snapshotFlow {
        musicVolume.first()
    }

    override suspend fun decreaseMusicVolume(): Flow<Result<Unit, AdjustVolumeError>> =
        callbackFlow {
            try {
                val currentMusicVolume = musicVolume.first()
                musicVolume.apply {
                    clear()
                    add(
                        (currentMusicVolume - 1).coerceIn(
                            minimumValue = 0,
                            maximumValue = 15
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
                val currentMusicVolume = musicVolume.first()
                musicVolume.apply {
                    clear()
                    add(
                        (currentMusicVolume + 1).coerceIn(
                            minimumValue = 0,
                            maximumValue = 15
                        )
                    )
                }

                send(Result.Success(Unit))
            } catch (e: Exception) {
                send(Result.Error(AdjustVolumeError.SomethingWentWrong))
            }

            awaitClose {}
        }

    override suspend fun getRadios(
        fetchFromRemote: Boolean
    ): Flow<Result<List<Radio>, GetRadiosError>> = flow {
        val shouldJustLoadFromCache = radioEntities.isNotEmpty() && !fetchFromRemote
        if (shouldJustLoadFromCache) {
            emit(Result.Success(radioEntities.map { it.toRadio() }))
            return@flow
        }

        if (!isNetworkAvailable) {
            emit(Result.Error(GetRadiosError.Network.Offline))
            return@flow
        }

        val mockEngine = MockEngine {
            val getRadiosResponseDto = if (getRadiosHttpStatusCode == HttpStatusCode.OK) {
                addDummyRadios()
                GetRadiosResponseDto(radioDtos = radioEntities.map { it.toRadioDto() })
            } else {
                GetRadiosResponseDto(radioDtos = emptyList())
            }

            respond(
                content = Json.encodeToString(getRadiosResponseDto),
                status = getRadiosHttpStatusCode,
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

        val response = testClient.get(urlString = MusicPlayerRepository.EndPoints.GetRadios.url)

        when (response.status) {
            HttpStatusCode.OK -> {
                val remoteRadioDtos = response.body<GetRadiosResponseDto>().radioDtos

                radioEntities.clear()
                radioEntities.addAll(remoteRadioDtos.map { it.toRadioEntity() })

                emit(Result.Success(radioEntities.map { it.toRadio() }))
            }
            else -> emit(Result.Error(GetRadiosError.Network.SomethingWentWrong))
        }
    }
}