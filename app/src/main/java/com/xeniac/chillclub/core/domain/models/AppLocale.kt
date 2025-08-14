package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.ui.unit.LayoutDirection
import com.xeniac.chillclub.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppLocale(
    val index: Int,
    val languageTag: String?,
    val localeString: String?,
    val layoutDirectionCompose: LayoutDirection,
    val layoutDirection: Int,
    @StringRes val localeTitleId: Int
) : Parcelable {
    data object Default : AppLocale(
        index = 0,
        languageTag = null,
        localeString = null,
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = -1, // LayoutDirection.UNDEFINED
        localeTitleId = R.string.core_locale_default
    )

    data object EnglishUS : AppLocale(
        index = 1,
        languageTag = "en-US",
        localeString = "en_US",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR,
        localeTitleId = R.string.core_locale_english_us
    )

    data object EnglishGB : AppLocale(
        index = 2,
        languageTag = "en-GB",
        localeString = "en_GB",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR,
        localeTitleId = R.string.core_locale_english_gb
    )

    data object FarsiIR : AppLocale(
        index = 3,
        languageTag = "fa-IR",
        localeString = "fa_IR",
        layoutDirectionCompose = LayoutDirection.Rtl,
        layoutDirection = android.util.LayoutDirection.RTL,
        localeTitleId = R.string.core_locale_farsi_ir
    )
}