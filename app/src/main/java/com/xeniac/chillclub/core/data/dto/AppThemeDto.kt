package com.xeniac.chillclub.core.data.dto

import androidx.appcompat.app.AppCompatDelegate

sealed class AppThemeDto(
    val setAppTheme: () -> Unit
) {
    data object Light : AppThemeDto(
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppThemeDto(
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )
}