package com.xeniac.chillclub.core.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.chillclub.core.data.local.entities.RadioEntity
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
    fun insertRadio() = runTest {
        val radioEntity = RadioEntity(
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
        dao.insertRadio(radioEntity)

        val radios = dao.getRadios()
        assertThat(radios).contains(radioEntity)
    }

    @Test
    fun insertRadios() = runTest {
        val dummyRadios = mutableListOf<RadioEntity>()
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

            dummyRadios.add(radioEntity)
        }

        dao.insertRadios(dummyRadios)

        val radios = dao.getRadios()
        assertThat(radios).isNotEmpty()
        assertThat(radios).containsExactlyElementsIn(dummyRadios)
    }

    @Test
    fun clearRadios() = runTest {
        val dummyRadios = mutableListOf<RadioEntity>()
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

            dummyRadios.add(radioEntity)
            dao.insertRadio(radioEntity)
        }

        val radios = dao.getRadios()
        assertThat(radios).isNotEmpty()
        assertThat(radios).containsExactlyElementsIn(dummyRadios)

        dao.clearRadios()

        val radiosAfterClear = dao.getRadios()
        assertThat(radiosAfterClear).isEmpty()
    }

    @Test
    fun deleteRadio() = runTest {
        val radioEntity = RadioEntity(
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
        dao.insertRadio(radioEntity)

        val radios = dao.getRadios()
        assertThat(radios).contains(radioEntity)

        dao.deleteRadio(radioEntity)

        val radiosAfterDelete = dao.getRadios()
        assertThat(radiosAfterDelete).doesNotContain(radioEntity)
    }

    @Test
    fun deleteRadioById() = runTest {
        val dummyRadios = mutableListOf<RadioEntity>()
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

            dummyRadios.add(radioEntity)
            dao.insertRadio(radioEntity)
        }

        val radios = dao.getRadios()
        assertThat(radios).isNotEmpty()
        assertThat(radios).containsExactlyElementsIn(dummyRadios)

        val testRadio = dummyRadios[Random.nextInt(from = 0, until = 10)]

        assertThat(radios).contains(testRadio)
        assertThat(testRadio.id).isNotNull()

        dao.deleteRadioById(testRadio.id!!)

        val radiosAfterDelete = dao.getRadios()
        assertThat(radiosAfterDelete).doesNotContain(testRadio)
    }

    @Test
    fun getRadios() = runTest {
        val dummyRadios = mutableListOf<RadioEntity>()
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

            dummyRadios.add(radioEntity)
            dao.insertRadio(radioEntity)
        }

        val radios = dao.getRadios()
        assertThat(radios).isNotEmpty()
        assertThat(radios).containsExactlyElementsIn(dummyRadios)
    }

    @Test
    fun observeRadios() = runTest {
        val dummyRadios = mutableListOf<RadioEntity>()
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

            dummyRadios.add(radioEntity)
            dao.insertRadio(radioEntity)
        }

        val radios = dao.observeRadios().first()
        assertThat(radios).isNotEmpty()
        assertThat(radios).containsExactlyElementsIn(dummyRadios)
    }

    @Test
    fun observeRadio() = runTest {
        val radioEntity = RadioEntity(
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
        dao.insertRadio(radioEntity)

        assertThat(radioEntity.id).isNotNull()
        val radio = dao.observeRadio(id = radioEntity.id!!).first()

        assertThat(radio).isEqualTo(radioEntity)
    }
}