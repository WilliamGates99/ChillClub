package com.xeniac.chillclub.feature_music_player.presensation

import android.Manifest
import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.rule.GrantPermissionRule
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.main_activity.MainActivity
import com.xeniac.chillclub.core.presentation.common.ui.navigation.MusicPlayerScreen
import com.xeniac.chillclub.core.presentation.common.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.chillclub.core.presentation.common.ui.theme.ChillClubTheme
import com.xeniac.chillclub.feature_music_player.presensation.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MusicPlayerScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule(order = 1)
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val intentsTestRule = IntentsRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.apply {
            activity.setContent {
                ChillClubTheme {
                    val testNavController = rememberNavController()

                    SetupRootNavGraph(
                        rootNavController = testNavController,
                        startDestination = MusicPlayerScreen
                    )
                }
            }
        }
    }

    @Test
    fun launchingMusicPlayerScreen_showsMusicPlayerScreen() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.music_player_app_bar_title_welcome)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.app_name).uppercase()).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithContentDescription(context.getString(R.string.music_player_btn_settings)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithTag(TestTags.BOTTOM_SHEET_HEADER).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnSettingsBtn_navigatesToSettingsScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription(context.getString(R.string.music_player_btn_settings)).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithText(context.getString(R.string.settings_app_bar_title)).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnRadioStationsBottomSheetHeader_opensBottomSheet() {
        composeTestRule.apply {
            onNodeWithTag(TestTags.BOTTOM_SHEET_HEADER).apply {
                assertExists()
                assertIsDisplayed()
                performClick()
            }

            onNodeWithTag(TestTags.BOTTOM_SHEET_RADIO_STATIONS).apply {
                assertExists()
                assertIsDisplayed()
            }
        }
    }
}