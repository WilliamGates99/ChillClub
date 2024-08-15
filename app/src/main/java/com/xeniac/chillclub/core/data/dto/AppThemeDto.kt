package com.xeniac.chillclub.core.data.dto

import androidx.appcompat.app.AppCompatDelegate

sealed class AppThemeDto(
    val index: Int,
    val setAppTheme: () -> Unit
) {
    data object Light : AppThemeDto(
        index = 1,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppThemeDto(
        index = 2,
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )
}