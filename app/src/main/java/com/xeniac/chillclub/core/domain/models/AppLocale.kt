package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppLocale(
    val index: Int,
    val languageTag: String?,
    val localeString: String?,
    val layoutDirectionCompose: LayoutDirection,
    val layoutDirection: Int
) : Parcelable {
    data object Default : AppLocale(
        index = 0,
        languageTag = null,
        localeString = null,
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = -1 // LayoutDirection.UNDEFINED
    )

    data object EnglishUS : AppLocale(
        index = 1,
        languageTag = "en-US",
        localeString = "en_US",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR
    )

    data object EnglishGB : AppLocale(
        index = 2,
        languageTag = "en-GB",
        localeString = "en_GB",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR
    )

    data object FarsiIR : AppLocale(
        index = 3,
        languageTag = "fa-IR",
        localeString = "fa_IR",
        layoutDirectionCompose = LayoutDirection.Rtl,
        layoutDirection = android.util.LayoutDirection.RTL
    )
}