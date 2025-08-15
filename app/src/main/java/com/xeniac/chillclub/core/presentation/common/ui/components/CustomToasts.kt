package com.xeniac.chillclub.core.presentation.common.ui.components

import android.content.Context
import android.widget.Toast
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.utils.UiText

fun Context.showShortToast(
    message: UiText,
    duration: Int = Toast.LENGTH_SHORT
) = Toast.makeText(
    /* context = */ this,
    /* text = */ message.asString(context = this),
    /* duration = */ duration
).show()

fun Context.showLongToast(
    message: UiText,
    duration: Int = Toast.LENGTH_LONG
) = Toast.makeText(
    /* context = */ this,
    /* text = */ message.asString(context = this),
    /* duration = */ duration
).show()

fun Context.showIntentAppNotFoundToast(
    duration: Int = Toast.LENGTH_LONG,
    message: UiText = UiText.StringResource(R.string.error_intent_app_not_found)
) = Toast.makeText(
    /* context = */ this,
    /* text = */ message.asString(context = this),
    /* duration = */ duration
).show()