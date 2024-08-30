package com.xeniac.chillclub.core.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.R
import com.xeniac.chillclub.feature_settings.presentation.utils.Constants

typealias AppNotFound = Boolean

object IntentHelper {

    fun openLinkInInAppBrowser(context: Context, urlString: String) {
        val intent = CustomTabsIntent.Builder().build().apply {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        intent.launchUrl(
            /* context = */ context,
            /* url = */ Uri.parse(urlString)
        )
    }

    /**
     * returns true if browser app was not found
     */
    fun openLinkInBrowser(context: Context, urlString: String): AppNotFound = try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(urlString)
            context.startActivity(this)
        }
        false
    } catch (e: ActivityNotFoundException) {
        true
    }

    /**
     * returns true if browser app was not found
     */
    fun openAppPageInStore(context: Context): AppNotFound = try {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(BuildConfig.URL_APP_STORE)
            setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
            context.startActivity(this)
        }
        false
    } catch (e: ActivityNotFoundException) {
        openLinkInBrowser(
            context = context,
            urlString = BuildConfig.URL_APP_STORE
        )
    }

    /**
     * returns true if browser app was not found
     */
    fun openAppUpdatePageInStore(context: Context): AppNotFound {
        val appStoreUrl = if (isAppInstalledFromGitHub()) {
            BuildConfig.URL_APP_STORE + "/releases/latest"
        } else BuildConfig.URL_APP_STORE

        return try {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(appStoreUrl)
                setPackage(BuildConfig.PACKAGE_NAME_APP_STORE)
                context.startActivity(this)
            }
            false
        } catch (e: ActivityNotFoundException) {
            openLinkInBrowser(
                context = context,
                urlString = appStoreUrl
            )
        }
    }

    /**
     * returns true if email app was not found
     */
    fun sendEmail(context: Context): AppNotFound = try {
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

        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")

            putExtra(
                /* name = */ Intent.EXTRA_EMAIL,
                /* value = */ arrayOf(Constants.EMAIL_CONTACT_US)
            )
            putExtra(
                /* name = */ Intent.EXTRA_SUBJECT,
                /* value = */ context.getString(R.string.app_name)
            )
            putExtra(
                /* name = */ Intent.EXTRA_TEXT,
                /* value = */ emailText
            )

            context.startActivity(
                Intent.createChooser(
                    /* target = */ this,
                    /* title = */ context.getString(
                        R.string.settings_about_contact_us_app_chooser_title
                    )
                )
            )
        }
        false
    } catch (e: ActivityNotFoundException) {
        true
    }

    /**
     * returns true if email app was not found
     */
    fun sendShareMessage(context: Context): AppNotFound = try {
        val shareMessage = context.getString(
            R.string.settings_about_share_message,
            context.getString(R.string.app_name),
            BuildConfig.URL_APP_STORE
        )

        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"

            putExtra(
                /* name = */ Intent.EXTRA_TEXT,
                /* value = */ shareMessage
            )

            context.startActivity(
                Intent.createChooser(
                    /* target = */ this,
                    /* title = */ context.getString(
                        R.string.settings_about_share_app_chooser_title
                    )
                )
            )
        }
        false
    } catch (e: ActivityNotFoundException) {
        true
    }
}