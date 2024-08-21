package com.xeniac.chillclub.feature_music_player.data.remote.repositories

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.chillclub.core.data.db.entities.RadioEntity
import com.xeniac.chillclub.core.domain.utils.Result
import com.xeniac.chillclub.feature_music_player.data.remote.dto.ChannelDto
import com.xeniac.chillclub.feature_music_player.data.remote.dto.GetRadiosResponseDto
import com.xeniac.chillclub.feature_music_player.data.remote.dto.SocialLinksDto
import com.xeniac.chillclub.feature_music_player.domain.models.Radio
import com.xeniac.chillclub.feature_music_player.domain.repositories.MusicPlayerRepository
import com.xeniac.chillclub.feature_music_player.domain.utils.GetRadiosError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeMusicPlayerRepositoryImpl @Inject constructor() : MusicPlayerRepository {

    private var isNetworkAvailable = false
    private var radioEntities = SnapshotStateList<RadioEntity>()
    private var getRadiosHttpStatusCode = HttpStatusCode.OK

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setGetRadiosHttpStatusCode(httpStatusCode: HttpStatusCode) {
        getRadiosHttpStatusCode = httpStatusCode
    }

    fun addDummyRadios() {
        repeat(times = 10) { index ->
            val radioEntity = RadioEntity(
                youtubeVideoId = "abc$index",
                title = "Test Title $index",
                channelDto = ChannelDto(
                    name = "Test Channel $index",
                    avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                    socialLinksDto = SocialLinksDto(
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
            if (getRadiosHttpStatusCode == HttpStatusCode.OK) {
                val getRadiosResponseDto = GetRadiosResponseDto(
                    radioDtos = radioEntities.map { it.toRadioDto() }
                )

                respond(
                    content = Json.encodeToString(getRadiosResponseDto),
                    status = getRadiosHttpStatusCode
                )
            } else {
                respond(
                    content = "",
                    status = HttpStatusCode.RequestTimeout
                )
            }
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