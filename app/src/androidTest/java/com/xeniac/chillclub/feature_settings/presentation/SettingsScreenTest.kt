package com.xeniac.chillclub.feature_settings.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.rule.GrantPermissionRule
import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.ui.navigation.MusicPlayerScreen
import com.xeniac.chillclub.core.presentation.common.ui.navigation.nav_graph.SetupRootNavGraph
import com.xeniac.chillclub.core.presentation.common.ui.theme.ChillClubTheme
import com.xeniac.chillclub.core.presentation.common.utils.TestTags.BTN_SETTINGS
import com.xeniac.chillclub.core.presentation.main_activity.MainActivity
import com.xeniac.chillclub.feature_settings.presentation.utils.Constants
import com.xeniac.chillclub.feature_settings.presentation.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class SettingsScreenTest {

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

            onNodeWithTag(testTag = BTN_SETTINGS).performClick()
        }
    }

    @Test
    fun launchingSettingsScreen_showsAppSettings() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_app_bar_title)).apply {
                assertExists()
                assertIsDisplayed()
            }

            onNodeWithText(
                text = context.getString(R.string.settings_general_title),
                ignoreCase = true
            ).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_general_theme_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_general_background_player_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_general_background_play_description)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(
                text = context.getString(R.string.settings_about_title),
                ignoreCase = true
            ).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_about_about_us_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_about_contact_us_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_about_source_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_about_donate_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_about_share_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(
                text = context.getString(R.string.settings_support_title),
                ignoreCase = true
            ).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_support_ask_question_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }

            onNodeWithText(context.getString(R.string.settings_support_privacy_policy_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickOnBackgroundPlaySwitch_negatesTheBackgroundPlay() {
        composeTestRule.apply {
            onNodeWithTag(testTag = TestTags.SWITCH_BACKGROUND_PLAYER).apply {
                assertExists()
                assertIsDisplayed()
                assertIsOn()

                performClick()

                assertIsOff()
            }
        }
    }

    @Test
    fun clickOnAboutUsBtn_opensAboutUsUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_about_about_us_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_ABOUT_US)
            )
        )
    }

    @Test
    fun clickOnContactUsBtn_opensEmailAppWithPrefilledInfo() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_about_contact_us_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        val deviceModel = context.getString(
            R.string.settings_support_contact_us_email_device_model,
            Build.MODEL
        )
        val androidVersion = context.getString(
            R.string.settings_support_contact_us_email_device_model,
            Build.VERSION.RELEASE
        )
        val appVersion = context.getString(
            R.string.settings_support_contact_us_email_app_version,
            BuildConfig.VERSION_NAME
        )
        val emailText = context.getString(
            R.string.settings_support_contact_us_email_text,
            deviceModel,
            androidVersion,
            appVersion
        )

        val sendEmailIntent = CoreMatchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_SENDTO),
            IntentMatchers.hasData(Uri.parse("mailto:")),
            IntentMatchers.hasExtra(
                /* key = */ Intent.EXTRA_EMAIL,
                /* value = */ arrayOf(Constants.EMAIL_CONTACT_US)
            ),
            IntentMatchers.hasExtra(
                /* key = */ Intent.EXTRA_SUBJECT,
                /* value = */ context.getString(R.string.app_name)
            ),
            IntentMatchers.hasExtra(
                /* key = */ Intent.EXTRA_TEXT,
                /* value = */ emailText
            )
        )

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
                IntentMatchers.hasExtra(
                    /* key = */ Intent.EXTRA_INTENT,
                    /* valueMatcher = */ sendEmailIntent
                ),
                IntentMatchers.hasExtra(
                    /* key = */ Intent.EXTRA_TITLE,
                    /* value = */ context.getString(
                        R.string.settings_about_contact_us_app_chooser_title
                    )
                )
            )
        )
    }

    @Test
    fun clickOnSourceBtn_opensSourceUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_about_source_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_SOURCE)
            )
        )
    }

    @Test
    fun clickOnDonateBtn_opensDonateUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_about_donate_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_DONATE)
            )
        )
    }

    @Test
    fun clickShareUsBtn_sendShareMessage() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_about_share_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        val shareMessage = context.getString(
            R.string.settings_about_share_message,
            context.getString(R.string.app_name),
            BuildConfig.URL_APP_STORE
        )

        val sendShareMessageIntent = CoreMatchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_SEND),
            IntentMatchers.hasType("text/plain"),
            IntentMatchers.hasExtra(
                /* key = */ Intent.EXTRA_TEXT,
                /* value = */ shareMessage
            )
        )

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
                IntentMatchers.hasExtra(
                    /* key = */ Intent.EXTRA_INTENT,
                    /* valueMatcher = */ sendShareMessageIntent
                ),
                IntentMatchers.hasExtra(
                    /* key = */ Intent.EXTRA_TITLE,
                    /* value = */ context.getString(
                        R.string.settings_about_share_app_chooser_title
                    )
                )
            )
        )
    }

    @Test
    fun clickOnAskQuestionBtn_opensAskQuestionUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_support_ask_question_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_ASK_QUESTION)
            )
        )
    }

    @Test
    fun clickOnPrivacyPolicyBtn_opensPrivacyPolicyUrlInBrowser() {
        composeTestRule.apply {
            onNodeWithText(context.getString(R.string.settings_support_privacy_policy_title)).apply {
                assertExists()
                performScrollTo()
                assertIsDisplayed()
                performClick()
            }
        }

        Intents.intended(
            CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData(Constants.URL_PRIVACY_POLICY)
            )
        )
    }
}