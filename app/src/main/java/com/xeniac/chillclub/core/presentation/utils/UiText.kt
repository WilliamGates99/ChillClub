package com.xeniac.chillclub.core.presentation.utils

import android.content.Context
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class UiText : Parcelable {
    data class DynamicString(val value: String) : UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: @RawValue Any
    ) : UiText()

    class PluralStringResource(
        @PluralsRes val resId: Int,
        val quantity: Int,
        vararg val args: @RawValue Any
    ) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id = resId, *args)
        is PluralStringResource -> pluralStringResource(id = resId, count = quantity, *args)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
        is PluralStringResource -> context.resources.getQuantityString(resId, quantity, *args)
    }

    fun getValueOrResId(): String = when (this) {
        is DynamicString -> value
        is StringResource -> resId.toString()
        is PluralStringResource -> resId.toString()
    }
}