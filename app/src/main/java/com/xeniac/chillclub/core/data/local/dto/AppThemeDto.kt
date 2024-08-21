package com.xeniac.chillclub.core.data.local.dto

import androidx.appcompat.app.AppCompatDelegate
import com.xeniac.chillclub.core.domain.models.AppTheme

sealed class AppThemeDto(
    val setAppTheme: () -> Unit
) {
    data object Light : AppThemeDto(
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppThemeDto(
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )

    fun toAppTheme(): AppTheme = when (this) {
        Light -> AppTheme.Light
        Dark -> AppTheme.Dark
    }
}