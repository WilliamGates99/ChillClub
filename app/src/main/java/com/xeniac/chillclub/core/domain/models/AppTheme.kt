package com.xeniac.chillclub.core.domain.models

import android.os.Parcelable
import androidx.appcompat.app.AppCompatDelegate
import com.xeniac.chillclub.core.data.local.dto.AppThemeDto
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppTheme(
    val setAppTheme: () -> Unit
) : Parcelable {
    data object Light : AppTheme(
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    )

    data object Dark : AppTheme(
        setAppTheme = { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    )

    fun toAppThemeDto(): AppThemeDto = when (this) {
        Light -> AppThemeDto.Light
        Dark -> AppThemeDto.Dark
    }
}