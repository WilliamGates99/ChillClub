package com.xeniac.chillclub.core.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.core.data.db.entities.RadioEntity
import com.xeniac.chillclub.feature_music_player.data.remote.dto.ChannelDto
import com.xeniac.chillclub.feature_music_player.data.remote.dto.SocialLinksDto
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

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ChillClubDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: ChillClubDatabase

    @Inject
    lateinit var dao: ChillClubDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertRadios() = runTest {
        val radioEntity = RadioEntity(
            youtubeVideoId = "abc123",
            title = "Test Title",
            channelDto = ChannelDto(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinksDto = SocialLinksDto(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioEntity(radioEntity)

        val radios = dao.getRadioEntities()
        assertThat(radios).contains(radioEntity)
    }

    @Test
    fun insertRadiosList() = runTest {
        val testEntities = mutableListOf<RadioEntity>()
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
            testEntities.add(radioEntity)
        }

        dao.insertRadioEntities(testEntities)

        val radios = dao.getRadioEntities()
        assertThat(radios.containsAll(testEntities)).isTrue()
    }

    @Test
    fun clearRadios() = runTest {
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
            dao.insertRadioEntity(radioEntity)
        }

        val radios = dao.getRadioEntities()
        assertThat(radios).isNotEmpty()

        dao.clearRadioEntities()

        val radiosAfterClear = dao.getRadioEntities()
        assertThat(radiosAfterClear).isEmpty()
    }

    @Test
    fun deleteRadio() = runTest {
        val radioEntity = RadioEntity(
            youtubeVideoId = "abc123",
            title = "Test Title",
            channelDto = ChannelDto(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinksDto = SocialLinksDto(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioEntity(radioEntity)

        val radios = dao.getRadioEntities()
        assertThat(radios).contains(radioEntity)

        dao.deleteRadioEntity(radioEntity)

        val radiosAfterDelete = dao.getRadioEntities()
        assertThat(radiosAfterDelete).doesNotContain(radioEntity)
    }

    @Test
    fun deleteRadioById() = runTest {
        val radioEntity = RadioEntity(
            youtubeVideoId = "abc123",
            title = "Test Title",
            channelDto = ChannelDto(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinksDto = SocialLinksDto(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioEntity(radioEntity)

        val radios = dao.getRadioEntities()
        assertThat(radios).contains(radioEntity)

        assertThat(radioEntity.id).isNotNull()
        dao.deleteRadioEntityById(radioEntity.id!!)

        val radiosAfterDelete = dao.getRadioEntities()
        assertThat(radiosAfterDelete).doesNotContain(radioEntity)
    }

    @Test
    fun getRadios() = runTest {
        val testEntities = mutableListOf<RadioEntity>()
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
            testEntities.add(radioEntity)
            dao.insertRadioEntity(radioEntity)
        }

        val radios = dao.getRadioEntities()
        assertThat(radios).isNotEmpty()
        assertThat(radios.containsAll(testEntities)).isTrue()
    }

    @Test
    fun observeRadios() = runTest {
        val testEntities = mutableListOf<RadioEntity>()
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
            testEntities.add(radioEntity)
            dao.insertRadioEntity(radioEntity)
        }

        val radios = dao.getRadioEntities()
        assertThat(radios).isNotEmpty()
        assertThat(radios.containsAll(testEntities)).isTrue()
    }

    @Test
    fun observeRadio() = runTest {
        val radioEntity = RadioEntity(
            youtubeVideoId = "abc123",
            title = "Test Title",
            channelDto = ChannelDto(
                name = "Test Channel",
                avatarUrl = "https://gravatar.com/avatar/b555351fc297b35d3eb1a7857740accd?s=800&d=mp&r=x",
                socialLinksDto = SocialLinksDto(
                    youtube = "https://www.youtube.com"
                )
            ),
            category = "Test",
            tags = listOf("tag1", "tag2"),
            id = 100
        )
        dao.insertRadioEntity(radioEntity)

        assertThat(radioEntity.id).isNotNull()
        val radio = dao.observeRadio(id = radioEntity.id!!).first()
        assertThat(radio).isEqualTo(radioEntity)
    }
}