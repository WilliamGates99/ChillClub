package com.xeniac.chillclub.core.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("findActivity function should be called in the context of an Activity")
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts(
            /* scheme = */ "package",
            /* ssp = */ packageName,
            /* fragment = */ null
        )
    ).also(::startActivity)
}

fun Activity.restartActivity() {
    Intent(
        /* packageContext = */ this,
        /* cls = */ this::class.java
    ).also {
        finish()
        startActivity(it)
    }
}