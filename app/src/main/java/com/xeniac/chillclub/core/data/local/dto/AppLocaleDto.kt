package com.xeniac.chillclub.core.data.local.dto

import androidx.compose.ui.unit.LayoutDirection

sealed class AppLocaleDto(
    val index: Int,
    val languageTag: String?,
    val localeString: String?,
    val layoutDirectionCompose: LayoutDirection,
    val layoutDirection: Int
) {
    data object Default : AppLocaleDto(
        index = 0,
        languageTag = null,
        localeString = null,
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = -1 // LayoutDirection.UNDEFINED
    )

    data object EnglishUS : AppLocaleDto(
        index = 1,
        languageTag = "en-US",
        localeString = "en_US",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR
    )

    data object EnglishGB : AppLocaleDto(
        index = 2,
        languageTag = "en-GB",
        localeString = "en_GB",
        layoutDirectionCompose = LayoutDirection.Ltr,
        layoutDirection = android.util.LayoutDirection.LTR
    )

    data object FarsiIR : AppLocaleDto(
        index = 3,
        languageTag = "fa-IR",
        localeString = "fa_IR",
        layoutDirectionCompose = LayoutDirection.Rtl,
        layoutDirection = android.util.LayoutDirection.RTL
    )
}