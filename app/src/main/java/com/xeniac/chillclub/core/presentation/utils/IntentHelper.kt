package com.xeniac.chillclub.core.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.xeniac.chillclub.BuildConfig

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
     * returns true if app was not found
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
}