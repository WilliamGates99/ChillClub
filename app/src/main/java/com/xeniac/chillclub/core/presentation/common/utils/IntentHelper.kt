package com.xeniac.chillclub.core.presentation.common.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.ui.components.showIntentAppNotFoundToast
import com.xeniac.chillclub.feature_settings.presentation.utils.Constants
import timber.log.Timber

fun Context.openLinkInInAppBrowser(
    urlString: String
) {
    try {
        val intent = CustomTabsIntent.Builder().build().apply {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        intent.launchUrl(
            /* context = */ this,
            /* url = */ urlString.toUri()
        )
    } catch (e: ActivityNotFoundException) {
        Timber.e("Open link in in-app browser Exception:")
        e.printStackTrace()

        openLinkInExternalBrowser(urlString)
    }
}

fun Context.openLinkInExternalBrowser(
    urlString: String
) {
    try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = urlString.toUri()
            startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        Timber.e("Open link in external browser Exception:")
        e.printStackTrace()

        showIntentAppNotFoundToast()
    }
}

fun Context.openAppPageInStore(
    customUrlString: String? = null
) {
    val appStoreUrlString = customUrlString ?: BuildConfig.URL_APP_STORE

    try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = appStoreUrlString.toUri()
            setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
            startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        Timber.e("Open app page in store Exception:")
        e.printStackTrace()

        openLinkInExternalBrowser(urlString = appStoreUrlString)
    }
}

fun Context.openAppUpdatePageInStore() {
    val appStoreUrl = if (isAppInstalledFromGitHub()) {
        BuildConfig.URL_APP_STORE + "/releases/latest"
    } else BuildConfig.URL_APP_STORE

    try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = appStoreUrl.toUri()
            setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
            startActivity(this)
        }
    } catch (e: ActivityNotFoundException) {
        Timber.e("Open app update page in store Exception:")
        e.printStackTrace()

        openLinkInExternalBrowser(urlString = appStoreUrl)
    }
}

fun Context.sendEmail() {
    try {
        val deviceModel = getString(
            R.string.settings_support_contact_us_email_device_model,
            Build.MODEL
        )
        val androidVersion = getString(
            R.string.settings_support_contact_us_email_android_version,
            Build.VERSION.RELEASE
        )
        val appVersion = getString(
            R.string.settings_support_contact_us_email_app_version,
            BuildConfig.VERSION_NAME
        )
        val emailText = getString(
            R.string.settings_support_contact_us_email_text,
            deviceModel,
            androidVersion,
            appVersion
        )

        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = "mailto:".toUri()

            putExtra(
                /* name = */ Intent.EXTRA_EMAIL,
                /* value = */ arrayOf(Constants.EMAIL_CONTACT_US)
            )
            putExtra(
                /* name = */ Intent.EXTRA_SUBJECT,
                /* value = */ getString(R.string.app_name)
            )
            putExtra(
                /* name = */ Intent.EXTRA_TEXT,
                /* value = */ emailText
            )

            startActivity(
                Intent.createChooser(
                    /* target = */ this,
                    /* title = */ getString(R.string.settings_about_contact_us_app_chooser_title)
                )
            )
        }
    } catch (e: ActivityNotFoundException) {
        Timber.e("Send email Exception:")
        e.printStackTrace()
        showIntentAppNotFoundToast()
    }
}

fun Context.sendShareMessage() {
    try {
        val shareMessage = getString(
            R.string.settings_about_share_message,
            getString(R.string.app_name),
            BuildConfig.URL_APP_STORE
        )

        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"

            putExtra(
                /* name = */ Intent.EXTRA_TEXT,
                /* value = */ shareMessage
            )

            startActivity(
                Intent.createChooser(
                    /* target = */ this,
                    /* title = */ getString(R.string.settings_about_share_app_chooser_title)
                )
            )
        }
    } catch (e: ActivityNotFoundException) {
        Timber.e("Send share message Exception:")
        e.printStackTrace()
        showIntentAppNotFoundToast()
    }
}