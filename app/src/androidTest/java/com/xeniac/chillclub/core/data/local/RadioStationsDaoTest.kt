package com.xeniac.chillclub.core.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.core.data.local.entities.RadioStationEntity
import com.xeniac.chillclub.core.domain.models.Channel
import com.xeniac.chillclub.core.domain.models.SocialLinks
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.random.Random

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RadioStationsDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: ChillClubDatabase

    @Inject
    lateinit var dao: RadioStationsDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertRadioStation() = runTest {
        val radioStationEntity = RadioStationEntity(
            youtubeVideoId = "videoId",
            title = "Test Title",
            channel = Channel(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinks = SocialLinks(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioStation(radioStationEntity)

        val radioStations = dao.getRadioStations()
        assertThat(radioStations).contains(radioStationEntity)
    }

    @Test
    fun insertRadioStationsStation() = runTest {
        val dummyRadioStations = mutableListOf<RadioStationEntity>()
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

            dummyRadioStations.add(radioStationEntity)
        }

        dao.insertRadioStations(dummyRadioStations)

        val radioStations = dao.getRadioStations()
        assertThat(radioStations).isNotEmpty()
        assertThat(radioStations).containsExactlyElementsIn(dummyRadioStations)
    }

    @Test
    fun clearRadioStations() = runTest {
        val dummyRadioStations = mutableListOf<RadioStationEntity>()
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

            dummyRadioStations.add(radioStationEntity)
            dao.insertRadioStation(radioStationEntity)
        }

        val radioStations = dao.getRadioStations()
        assertThat(radioStations).isNotEmpty()
        assertThat(radioStations).containsExactlyElementsIn(dummyRadioStations)

        dao.clearRadioStations()

        val radiosAfterClear = dao.getRadioStations()
        assertThat(radiosAfterClear).isEmpty()
    }

    @Test
    fun deleteRadioStation() = runTest {
        val radioStationEntity = RadioStationEntity(
            youtubeVideoId = "videoId",
            title = "Test Title",
            channel = Channel(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinks = SocialLinks(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioStation(radioStationEntity)

        val radioStations = dao.getRadioStations()
        assertThat(radioStations).contains(radioStationEntity)

        dao.deleteRadioStation(radioStationEntity)

        val radiosAfterDelete = dao.getRadioStations()
        assertThat(radiosAfterDelete).doesNotContain(radioStationEntity)
    }

    @Test
    fun deleteRadioStationById() = runTest {
        val dummyRadioStations = mutableListOf<RadioStationEntity>()
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

            dummyRadioStations.add(radioStationEntity)
            dao.insertRadioStation(radioStationEntity)
        }

        val radioStations = dao.getRadioStations()
        assertThat(radioStations).isNotEmpty()
        assertThat(radioStations).containsExactlyElementsIn(dummyRadioStations)

        val testRadio = dummyRadioStations[Random.nextInt(from = 0, until = 10)]

        assertThat(radioStations).contains(testRadio)
        assertThat(testRadio.id).isNotNull()

        dao.deleteRadioStationById(testRadio.id!!)

        val radiosAfterDelete = dao.getRadioStations()
        assertThat(radiosAfterDelete).doesNotContain(testRadio)
    }

    @Test
    fun getRadioStations() = runTest {
        val dummyRadioStations = mutableListOf<RadioStationEntity>()
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

            dummyRadioStations.add(radioStationEntity)
            dao.insertRadioStation(radioStationEntity)
        }

        val radioStations = dao.getRadioStations()
        assertThat(radioStations).isNotEmpty()
        assertThat(radioStations).containsExactlyElementsIn(dummyRadioStations)
    }

    @Test
    fun observeRadioStationStations() = runTest {
        val dummyRadioStations = mutableListOf<RadioStationEntity>()
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

            dummyRadioStations.add(radioStationEntity)
            dao.insertRadioStation(radioStationEntity)
        }

        val radioStations = dao.observeRadioStations().first()
        assertThat(radioStations).isNotEmpty()
        assertThat(radioStations).containsExactlyElementsIn(dummyRadioStations)
    }

    @Test
    fun observeRadioStation() = runTest {
        val radioStationEntity = RadioStationEntity(
            youtubeVideoId = "videoId",
            title = "Test Title",
            channel = Channel(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinks = SocialLinks(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioStation(radioStationEntity)

        assertThat(radioStationEntity.id).isNotNull()
        val radio = dao.observeRadioStation(id = radioStationEntity.id!!).first()

        assertThat(radio).isEqualTo(radioStationEntity)
    }
}